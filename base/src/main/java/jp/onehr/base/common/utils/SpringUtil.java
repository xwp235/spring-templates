package jp.onehr.base.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import io.micrometer.common.lang.NonNullApi;
import jp.onehr.base.InitTemplateApplication;
import jp.onehr.base.common.exception.UtilException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.*;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@NonNullApi
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    /**
     * "@PostConstruct"注解标记的类中，由于ApplicationContext还未加载，导致空指针<br>
     * 因此实现BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     */
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
     *
     * @return {@link ListableBeanFactory}
     */
    public static ListableBeanFactory getBeanFactory() {
        var factory = null == beanFactory ? applicationContext : beanFactory;
        if (null == factory) {
            throw new UtilException("No ConfigurableListableBeanFactory or ApplicationContext injected, maybe not in the Spring environment?");
        }
        return factory;
    }

    /**
     * 获取{@link ConfigurableListableBeanFactory}
     *
     * @return {@link ConfigurableListableBeanFactory}
     */
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws UtilException {
        ConfigurableListableBeanFactory factory;
        if (null != beanFactory) {
            factory = beanFactory;
        } else if (applicationContext instanceof ConfigurableApplicationContext) {
            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        } else {
            throw new UtilException("No ConfigurableListableBeanFactory from context!");
        }
        return factory;
    }

    /**
     * 通过name获取 Bean
     *
     * @param <T>  Bean类型
     * @param name Bean名称
     * @return Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean
     *
     * @param <T>   Bean类型
     * @param clazz Bean类
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param <T>   bean类型
     * @param name  Bean名称
     * @param clazz bean类型
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 通过类型参考返回带泛型参数的Bean
     *
     * @param reference 类型参考，用于持有转换后的泛型类型
     * @param <T>       Bean类型
     * @return 带泛型参数的Bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(TypeReference<T> reference) {
        final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
        final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
        final String[] beanNames = applicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    /**
     * 获取指定类型对应的所有Bean，包括子类
     *
     * @param <T>  Bean类型
     * @param type 类、接口，null表示获取所有bean
     * @return 类型对应的bean，key是bean注册的name，value是Bean
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 获取指定类型对应的Bean名称，包括子类
     *
     * @param type 类、接口，null表示获取所有bean名称
     * @return bean名称
     */
    public static String[] getBeanNamesForType(Class<?> type) {
        return applicationContext.getBeanNamesForType(type);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     * @return 属性值
     */
    public static String getProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : "default";
    }

    /**
     * 动态向Spring注册Bean
     *
     * @param <T>      Bean类型
     * @param beanName 名称
     * @param bean     bean
     */
    public static <T> void registerBean(String beanName, T bean) {
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName, bean);
    }

    /**
     * 注销bean
     * 将Spring中的bean注销，请谨慎使用
     *
     * @param beanName bean名称
     */
    public static void unregisterBean(String beanName) {
        ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        Object beanInstance = factory.getSingleton(beanName);
        if (beanInstance instanceof DisposableBean) {
            try {
                ((DisposableBean) beanInstance).destroy();
            } catch (Exception e) {
                throw new UtilException("Can not unregister bean, execute destroy method error occurred!", e);
            }
        }
        if (factory instanceof DefaultSingletonBeanRegistry registry) {
            registry.destroySingleton(beanName);
        } else {
            throw new UtilException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
        }
    }

    /**
     * 发布事件
     *
     * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
     * @since 5.7.12
     */
    public static void publishEvent(ApplicationEvent event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }

    /**
     * 发布事件
     * Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
     *
     * @param event 待发布的事件
     */
    public static void publishEvent(Object event) {
        if (null != applicationContext) {
            applicationContext.publishEvent(event);
        }
    }

    public static String getMessage(String key,String ...values) {
        var locale = LocaleContextHolder.getLocale();
        var messageSource = getBean(MessageSource.class);
        if (values.length > 0) {
            return messageSource.getMessage(key,values, locale);
        }
        return messageSource.getMessage(key,new Object[]{}, locale);
    }

    public static String getLocale() {
        var localeStr =  LocaleContextHolder.getLocale().toString();
        if (StringUtils.isBlank(localeStr)) {
            return "en";
        }
        return localeStr.split("_")[0];
    }

    public static void restartApp() {
        try (var executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.schedule(SpringUtil::internalRestartApp, 1, TimeUnit.SECONDS);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void loadEnvFile(String[] args) {
        try {
            var useDefaultConfigFilepath = false;
            if (args.length == 0) {
                useDefaultConfigFilepath = true;
                args = new String[] {
                  System.getProperty("user.dir")
                };
            }
            var configFilePath = args[0];

            if (useDefaultConfigFilepath) {
                configFilePath = configFilePath + File.separator + "env";
            }
            var separatorIndex = configFilePath.lastIndexOf(File.separator);
            var configFileDir = configFilePath.substring(0, separatorIndex);
            var configFilename = configFilePath.substring(separatorIndex + 1);

            // 获取当前系统属性
            var currentEnv = System.getenv();
            var dotenv = Dotenv.configure()
                    .directory(configFileDir)
                    .filename(configFilename)
                    .load();
            dotenv.entries().forEach(entry -> {
                var key = entry.getKey();
                var val = entry.getValue();
                if (!currentEnv.containsKey(key)) {
                    if ("LOGGING_STRUCTURED".equals(key)) {
                        var logFilename = "classpath:logback-spring.xml";
                        if ("on".equals(val)) {
                            logFilename = "classpath:logback-spring-structured.xml";
                        }
                        System.setProperty("logging.config", logFilename);
                    }
                    System.setProperty(key, val);
                }
            });
        } catch (DotenvException e) {
            var errorMessage = e.getMessage();
            var locale = Locale.getDefault();
            var bundle = ResourceBundle.getBundle("messages/lang", locale);
            if (errorMessage.contains("Could not find")) {
                logger.warn(bundle.getString("useDefaultAppConfig"));
                throw e;
            } else if (errorMessage.contains("Malformed entry")) {
                var param = errorMessage.substring(errorMessage.indexOf('y') + 2);
                logger.warn(bundle.getString("appConfigMalformed"), param);
                throw e;
            } else {
                logger.error(bundle.getString("serverInternalError"), e);
                throw e;
            }
        }
    }

    private synchronized static void internalRestartApp() {
        var args = getBean(ApplicationArguments.class);
        new Thread(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new UtilException("Execute needed sleep is interrupted!", e);
            }
            try {
                SpringApplication.exit(applicationContext);
            } catch (Exception e) {
                throw new UtilException("Exit app error occurred!");
            }
            try {
                var sourceArgs = args.getSourceArgs();
                loadEnvFile(sourceArgs);
                SpringApplication.run(InitTemplateApplication.class, sourceArgs);
            } catch (Exception e) {
                throw new UtilException("Restart app error occurred!", e);
            }
        }).start();
    }

}
