package cc.nyanyanya.backend.common.config

import com.baomidou.mybatisplus.annotation.IEnum
import com.baomidou.mybatisplus.core.MybatisParameterHandler
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.baomidou.mybatisplus.core.handlers.CompositeEnumTypeHandler
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler
import com.baomidou.mybatisplus.extension.handlers.GsonTypeHandler
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean
import com.github.yulichang.base.MPJBaseMapper
import com.github.yulichang.extension.kt.KtLambdaWrapper
import com.github.yulichang.wrapper.MPJLambdaWrapper
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.ibatis.annotations.DeleteProvider
import org.apache.ibatis.annotations.InsertProvider
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.annotations.UpdateProvider
import org.apache.ibatis.cache.decorators.FifoCache
import org.apache.ibatis.cache.decorators.LruCache
import org.apache.ibatis.cache.decorators.SoftCache
import org.apache.ibatis.cache.decorators.WeakCache
import org.apache.ibatis.cache.impl.PerpetualCache
import org.apache.ibatis.executor.Executor
import org.apache.ibatis.executor.parameter.ParameterHandler
import org.apache.ibatis.executor.resultset.ResultSetHandler
import org.apache.ibatis.executor.statement.BaseStatementHandler
import org.apache.ibatis.executor.statement.RoutingStatementHandler
import org.apache.ibatis.executor.statement.StatementHandler
import org.apache.ibatis.javassist.util.proxy.ProxyFactory
import org.apache.ibatis.javassist.util.proxy.RuntimeSupport
import org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl
import org.apache.ibatis.logging.log4j2.Log4j2Impl
import org.apache.ibatis.logging.nologging.NoLoggingImpl
import org.apache.ibatis.logging.slf4j.Slf4jImpl
import org.apache.ibatis.logging.stdout.StdOutImpl
import org.apache.ibatis.mapping.BoundSql
import org.apache.ibatis.reflection.TypeParameterResolver
import org.apache.ibatis.scripting.defaults.RawLanguageDriver
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.mapper.MapperFactoryBean
import org.mybatis.spring.mapper.MapperScannerConfigurer
import org.springframework.aot.generate.GenerationContext
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode
import org.springframework.beans.factory.aot.BeanRegistrationExcludeFilter
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.config.ConstructorArgumentValues
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
import org.springframework.beans.factory.support.RegisteredBean
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.ResolvableType
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * This configuration will move to mybatis-spring-native.
 */
@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(
    MyBatisNativeConfiguration.MyBaitsRuntimeHintsRegistrar::class
)
class MyBatisNativeConfiguration {
    @Bean
    fun myBatisBeanFactoryInitializationAotProcessor(): MyBatisBeanFactoryInitializationAotProcessor {
        return MyBatisBeanFactoryInitializationAotProcessor()
    }

    internal class MyBaitsRuntimeHintsRegistrar : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            Stream.of(
                RawLanguageDriver::class.java,  // TODO 增加了MybatisXMLLanguageDriver.class
                XMLLanguageDriver::class.java, MybatisXMLLanguageDriver::class.java,
                RuntimeSupport::class.java,
                ProxyFactory::class.java,
                Slf4jImpl::class.java,
                org.apache.ibatis.logging.Log::class.java,
                JakartaCommonsLoggingImpl::class.java,
                Log4j2Impl::class.java,
                Jdk14LoggingImpl::class.java,
                StdOutImpl::class.java,
                NoLoggingImpl::class.java,
                SqlSessionFactory::class.java,
                PerpetualCache::class.java,
                FifoCache::class.java,
                LruCache::class.java,
                SoftCache::class.java,
                WeakCache::class.java,  //TODO 增加了MybatisSqlSessionFactoryBean.class
                SqlSessionFactoryBean::class.java, MybatisSqlSessionFactoryBean::class.java,
                ArrayList::class.java,
                HashMap::class.java,
                TreeSet::class.java,
                HashSet::class.java
            ).forEach { x: Class<out Any>? ->
                hints.reflection().registerType(x!!, *MemberCategory.entries.toTypedArray())
            }
            Stream.of(
                "org/apache/ibatis/builder/xml/*.dtd",
                "org/apache/ibatis/builder/xml/*.xsd"
            ).forEach { include: String? -> hints.resources().registerPattern(include!!) }

            hints.serialization().registerType(SerializedLambda::class.java)
            hints.serialization().registerType(SFunction::class.java)
            hints.serialization().registerType(java.lang.invoke.SerializedLambda::class.java)
            hints.reflection().registerType(SFunction::class.java)
            hints.reflection().registerType(SerializedLambda::class.java)
            hints.reflection().registerType(java.lang.invoke.SerializedLambda::class.java)

            hints.proxies().registerJdkProxy(StatementHandler::class.java)
            hints.proxies().registerJdkProxy(Executor::class.java)
            hints.proxies().registerJdkProxy(ResultSetHandler::class.java)
            hints.proxies().registerJdkProxy(ParameterHandler::class.java)

            //        hints.reflection().registerType(MybatisPlusInterceptor.class);
            hints.reflection().registerType(AbstractWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(LambdaQueryWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(LambdaUpdateWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(UpdateWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(QueryWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(MPJBaseMapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(MPJLambdaWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(KtQueryWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(KtUpdateWrapper::class.java, *MemberCategory.entries.toTypedArray())
            hints.reflection().registerType(KtLambdaWrapper::class.java, *MemberCategory.entries.toTypedArray())

            hints.reflection().registerType(BoundSql::class.java, MemberCategory.DECLARED_FIELDS)
            hints.reflection().registerType(RoutingStatementHandler::class.java, MemberCategory.DECLARED_FIELDS)
            hints.reflection().registerType(BaseStatementHandler::class.java, MemberCategory.DECLARED_FIELDS)
            hints.reflection().registerType(MybatisParameterHandler::class.java, MemberCategory.DECLARED_FIELDS)


            hints.reflection().registerType(IEnum::class.java, MemberCategory.INVOKE_PUBLIC_METHODS)
            // register typeHandler
            hints.reflection()
                .registerType(CompositeEnumTypeHandler::class.java, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
            hints.reflection().registerType(FastjsonTypeHandler::class.java, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
            hints.reflection().registerType(GsonTypeHandler::class.java, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
            hints.reflection().registerType(JacksonTypeHandler::class.java, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
            hints.reflection()
                .registerType(MybatisEnumTypeHandler::class.java, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS)
        }
    }

    class MyBatisBeanFactoryInitializationAotProcessor

        : BeanFactoryInitializationAotProcessor, BeanRegistrationExcludeFilter {
        private val excludeClasses: MutableSet<Class<*>> = HashSet()

        init {
            excludeClasses.add(MapperScannerConfigurer::class.java)
        }

        override fun isExcludedFromAotProcessing(registeredBean: RegisteredBean): Boolean {
            return excludeClasses.contains(registeredBean.beanClass)
        }

        override fun processAheadOfTime(beanFactory: ConfigurableListableBeanFactory): BeanFactoryInitializationAotContribution? {
            val beanNames = beanFactory.getBeanNamesForType(MapperFactoryBean::class.java)
            if (beanNames.size == 0) {
                return null
            }
            return BeanFactoryInitializationAotContribution { context: GenerationContext, code: BeanFactoryInitializationCode? ->
                val hints = context.runtimeHints
                for (beanName in beanNames) {
                    val beanDefinition = beanFactory.getBeanDefinition(beanName.substring(1))
                    val mapperInterface = beanDefinition.propertyValues.getPropertyValue("mapperInterface")
                    if (mapperInterface != null && mapperInterface.value != null) {
                        val mapperInterfaceType = mapperInterface.value as Class<*>?
                        if (mapperInterfaceType != null) {
                            registerReflectionTypeIfNecessary(mapperInterfaceType, hints)
                            hints.proxies().registerJdkProxy(mapperInterfaceType)
                            hints.resources()
                                .registerPattern(mapperInterfaceType.name.replace('.', '/') + ".xml")
                            registerMapperRelationships(mapperInterfaceType, hints)
                        }
                    }
                }
            }
        }

        private fun registerMapperRelationships(mapperInterfaceType: Class<*>, hints: RuntimeHints) {
            val methods = ReflectionUtils.getAllDeclaredMethods(mapperInterfaceType)
            for (method in methods) {
                if (method.declaringClass != Any::class.java) {
                    ReflectionUtils.makeAccessible(method)
                    registerSqlProviderTypes(
                        method, hints,
                        SelectProvider::class.java,
                        Function { obj: SelectProvider -> obj.value::class.java },
                        Function { obj: SelectProvider -> obj.type::class.java })
                    registerSqlProviderTypes(
                        method, hints,
                        InsertProvider::class.java,
                        Function { obj: InsertProvider -> obj.value::class.java },
                        Function { obj: InsertProvider -> obj.type::class.java })
                    registerSqlProviderTypes(
                        method, hints,
                        UpdateProvider::class.java,
                        Function { obj: UpdateProvider -> obj.value::class.java },
                        Function { obj: UpdateProvider -> obj.type::class.java })
                    registerSqlProviderTypes(
                        method, hints,
                        DeleteProvider::class.java,
                        Function { obj: DeleteProvider -> obj.value::class.java },
                        Function { obj: DeleteProvider -> obj.type::class.java })
                    val returnType = MyBatisMapperTypeUtils.resolveReturnClass(mapperInterfaceType, method)
                    registerReflectionTypeIfNecessary(returnType!!, hints)
                    MyBatisMapperTypeUtils.resolveParameterClasses(mapperInterfaceType, method)
                        ?.forEach(Consumer { x: Class<*>? -> x?.let { registerReflectionTypeIfNecessary(it, hints) } })
                }
            }
        }

        @SafeVarargs
        private fun <T : Annotation?> registerSqlProviderTypes(
            method: Method,
            hints: RuntimeHints,
            annotationType: Class<T>,
            vararg providerTypeResolvers: Function<T, Class<*>>
        ) {
            for (annotation in method.getAnnotationsByType(annotationType)) {
                for (providerTypeResolver in providerTypeResolvers) {
                    registerReflectionTypeIfNecessary(providerTypeResolver.apply(annotation), hints)
                }
            }
        }

        private fun registerReflectionTypeIfNecessary(type: Class<*>, hints: RuntimeHints) {
            if (!type.isPrimitive && !type.name.startsWith("java")) {
                hints.reflection().registerType(type, *MemberCategory.entries.toTypedArray())
            }
        }
    }

    internal object MyBatisMapperTypeUtils {
        fun resolveReturnClass(mapperInterface: Class<*>?, method: Method): Class<*>? {
            val resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface)
            return typeToClass(resolvedReturnType, method.returnType)
        }

        fun resolveParameterClasses(mapperInterface: Class<*>?, method: Method): MutableSet<Class<*>?>? {
            return Stream.of(*TypeParameterResolver.resolveParamTypes(method, mapperInterface))
                .map { x: Type -> typeToClass(x, if (x is Class<*>) x else Any::class.java) }
                .collect(Collectors.toSet())
        }

        private fun typeToClass(src: Type, fallback: Class<*>): Class<*>? {
            var result: Class<*>? = null
            if (src is Class<*>) {
                result = if (src.isArray) {
                    src.componentType
                } else {
                    src
                }
            } else if (src is ParameterizedType) {
                val parameterizedType = src
                val index = if (parameterizedType.rawType is Class<*>
                    && MutableMap::class.java.isAssignableFrom(parameterizedType.rawType as Class<*>)
                    && parameterizedType.actualTypeArguments.size > 1
                ) 1 else 0
                val actualType = parameterizedType.actualTypeArguments[index]
                result = typeToClass(actualType, fallback)
            }
            if (result == null) {
                result = fallback
            }
            return result
        }
    }

    class MyBatisMapperFactoryBeanPostProcessor : MergedBeanDefinitionPostProcessor, BeanFactoryAware {
        private var beanFactory: ConfigurableBeanFactory? = null

        override fun setBeanFactory(beanFactory: BeanFactory) {
            this.beanFactory = beanFactory as ConfigurableBeanFactory
        }

        override fun postProcessMergedBeanDefinition(
            beanDefinition: RootBeanDefinition,
            beanType: Class<*>,
            beanName: String
        ) {
            if (ClassUtils.isPresent(
                    MAPPER_FACTORY_BEAN,
                    beanFactory!!.beanClassLoader
                )
            ) {
                resolveMapperFactoryBeanTypeIfNecessary(beanDefinition)
            }
        }

        private fun resolveMapperFactoryBeanTypeIfNecessary(beanDefinition: RootBeanDefinition) {
            if (!beanDefinition.hasBeanClass() || !MapperFactoryBean::class.java.isAssignableFrom(beanDefinition.beanClass)) {
                return
            }
            if (beanDefinition.resolvableType.hasUnresolvableGenerics()) {
                val mapperInterface = getMapperInterface(beanDefinition)
                if (mapperInterface != null) {
                    // Exposes a generic type information to context for prevent early initializing
                    val constructorArgumentValues = ConstructorArgumentValues()
                    constructorArgumentValues.addGenericArgumentValue(mapperInterface)
                    beanDefinition.constructorArgumentValues = constructorArgumentValues
                    beanDefinition.setTargetType(
                        ResolvableType.forClassWithGenerics(
                            beanDefinition.beanClass,
                            mapperInterface
                        )
                    )
                }
            }
        }

        private fun getMapperInterface(beanDefinition: RootBeanDefinition): Class<*>? {
            try {
                return beanDefinition.propertyValues["mapperInterface"] as Class<*>?
            } catch (e: Exception) {
                LOG.debug("Fail getting mapper interface type.", e)
                return null
            }
        }

        companion object {
            private val LOG: Log = LogFactory.getLog(
                MyBatisMapperFactoryBeanPostProcessor::class.java
            )

            private const val MAPPER_FACTORY_BEAN = "org.mybatis.spring.mapper.MapperFactoryBean"
        }
    }

    companion object {
        @Bean
        fun myBatisMapperFactoryBeanPostProcessor(): MyBatisMapperFactoryBeanPostProcessor {
            return MyBatisMapperFactoryBeanPostProcessor()
        }
    }
}