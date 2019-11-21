package com.github.smartbuf.springcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configure for {@link SmartbufMessageConverter}.
 * <p>
 * It could expose an context-type named 'application/x-smartbuf',
 * which has higher performance and smaller bytecodes than normal 'application/json'.
 *
 * @author sulin
 * @since 2019-11-21 14:33:41
 */
@Configuration
@ConditionalOnMissingBean({SmartbufMessageConverter.class})
public class SmartbufAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartbufAutoConfiguration.class);

    /**
     * Expose the {@link SmartbufMessageConverter} to Spring,
     * add support for context-type named 'application/x-smartbuf'
     *
     * @return The unique SmartbufMessageConverter instance
     */
    @Bean
    public SmartbufMessageConverter smartbufMessageConverter() {
        LOGGER.info("initialize SmartbufMessageConverter for supporting 'application/x-smartbuf' context-type");
        return new SmartbufMessageConverter();
    }

}
