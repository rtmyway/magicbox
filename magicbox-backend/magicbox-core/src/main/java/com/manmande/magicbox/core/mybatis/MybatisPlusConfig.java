package com.manmande.magicbox.core.mybatis;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.manmande.magicbox.core.mybatis.handler.AutoFillHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Bean
    public Resource[] mapperResources() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/*.xml");
        return resources;
    }

    /**
     * 新增分页拦截器，并设置数据库类型为mysql
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }



    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration config = new MybatisConfiguration();
        config.setMapUnderscoreToCamelCase(true);
        config.setJdbcTypeForNull(JdbcType.NULL);
        config.setCallSettersOnNulls(true);
        config.setCacheEnabled(false);
        config.setLogImpl(StdOutImpl.class);
        config.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
        return config;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(mapperResources());
        sqlSessionFactory.setConfiguration(this.mybatisConfiguration());
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setBanner(false);
        globalConfig.setMetaObjectHandler(new AutoFillHandler());
        sqlSessionFactory.setGlobalConfig(globalConfig);


        sqlSessionFactory.setPlugins(new Interceptor[]{mybatisPlusInterceptor()});
        return sqlSessionFactory.getObject();
    }
}