package com.github.smartbuf.springcloud.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author sulin
 * @since 2019-11-18 11:28:31
 */
@Slf4j
public class NetMonitor {

    private int pid;

    private long bytesIn;
    private long bytesOut;

    private Thread thread;

    public NetMonitor() {
        this.pid = PIDUtils.getPid();
    }

    public void start() {
        long[] deltaTimes = queryNetStatistics();

        this.thread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    long[] times = queryNetStatistics();
                    this.bytesIn = times[0] - deltaTimes[0];
                    this.bytesOut = times[1] - deltaTimes[1];
                } catch (Exception e) {
                    log.error("monitor error: ", e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        });
        this.thread.setDaemon(true);
        this.thread.start();
    }

    public synchronized void stop() {
        this.thread.interrupt();
    }

    public long getBytesIn() {
        return bytesIn;
    }

    public long getBytesOut() {
        return bytesOut;
    }

    /**
     * Call nettop to update bytes_in and bytes_out
     */
    private long[] queryNetStatistics() {
        ProcessBuilder ps = new ProcessBuilder("nettop", "-l1", "-x", "-p " + pid);
        ps.redirectErrorStream(true);
        Process process = null;
        long[] bytes = new long[2];
        try {
            process = ps.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (process.isAlive()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (!line.contains("java." + pid)) {
                    continue;
                }
                String[] parts = line.split(" +");
                bytes[0] = Long.parseLong(parts[2]);
                bytes[1] = Long.parseLong(parts[3]);
            }
            process.waitFor();
            reader.close();
        } catch (Throwable e) {
            log.error("call nettop failed: ", e);
            if (process != null) {
                process.destroy();
            }
        }
        return bytes;
    }

}
