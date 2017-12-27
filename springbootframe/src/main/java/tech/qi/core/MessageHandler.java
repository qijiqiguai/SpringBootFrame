package tech.qi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
* 消息配置处理类,在messageSource上封装了一层，因为消息提示涉及到太多的类，
 * 以后万一换messageSource直接改这个类就可以了
 *
 * @author wangqi
 */
@Component
public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    ResourceBundleMessageSource messageSource;

    /**
     * 提示类消息出错捕获异常，避免正常逻辑导致代码无法执行
     * @param code
     * @param args
     * @return
     */
    public String getMessage(String code, Object[] args){
        try {
            return messageSource.getMessage(code, args, Locale.SIMPLIFIED_CHINESE);
        } catch (Exception e) {
            logger.error("获取代码为["+code+"]的消息配置异常",e);
            return "出错了,未找到相关提示信息";
        }
    }

    public String getMessage(String code){
        return getMessage(code, null);
    }

    /**
     * 从spring上下文中获取
     * @return
     */
    private static MessageSource getMessageSourceAccessor(){
        return WebContextHolder.getInstance().getMessageSource();
    }

}
