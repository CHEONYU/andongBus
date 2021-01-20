package co.kr.smartplusteam.luna.study.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "co.kr.smartplusteam.luna.study.Repository",
        entityManagerFactoryRef = "andongBusEntityManager",
        transactionManagerRef = "andongBusTransactionManager"
)

public class MysqlConfig {
    @Autowired
    private Environment env;

    private static final String prefix = "spring.datasource.";

    @Bean(name="andongBusEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean andongBusEntityManager() {
        LocalContainerEntityManagerFactoryBean em  = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(andongBusDataSource());
        em.setPackagesToScan( new String[] { "co.kr.smartplusteam.luna.study.vo" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();
        em.setJpaPropertyMap(jpaProperties);

        return em;
    }

    @Bean
    @Primary
    public HikariDataSource andongBusDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl( env.getProperty(prefix+"url") );
        config.setUsername(env.getProperty(prefix+"username"));
        config.setPassword(env.getProperty(prefix+"password"));
//        config.setMaxLifetime( Long.parseLong(env.getProperty(prefix+"hikari.max-lifetime")) );
//        config.setConnectionTimeout(Long.parseLong( env.getProperty(prefix+"hikari.connection-timeout")));
//        config.setValidationTimeout(Long.parseLong( env.getProperty(prefix+"hikari.validation-timeout")));
//        config.addDataSourceProperty( "cachePrepStmts" ,  env.getProperty(prefix+"hikari.data-source-properties.cachePrepStmts"));
//        config.addDataSourceProperty( "prepStmtCacheSize" , env.getProperty(prefix+"hikari.data-source-properties.prepStmtCacheSize"));
//        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , env.getProperty(prefix+"hikari.data-source-properties.prepStmtCacheSqlLimit") );
//        config.addDataSourceProperty( "useServerPrepStmts" , env.getProperty(prefix+"hikari.data-source-properties.useServerPrepStmts") );

        HikariDataSource dataSource = new HikariDataSource(config);

        return dataSource;
    }


    @Bean(name="andongBusTransactionManager")
    @Primary
    public PlatformTransactionManager dbMallTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(andongBusEntityManager().getObject());

        return transactionManager;
    }
}
