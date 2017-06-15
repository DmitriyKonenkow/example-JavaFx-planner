package ru.spb.konenkov;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;

/**
 * Created by dmko1016 on 26.12.2016.
 */
@Configuration
@ComponentScan(basePackages = {"ru.spb.konenkov"})
@EnableJpaRepositories("ru.spb.konenkov.persistence")
public class SpringStandaloneConfig {

    @Bean
    public static LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPersistenceXmlLocation("classpath*:META-INF/persistence.xml");
        return emf;
    }

    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    @Autowired
    @Scope("prototype")
    public FXMLLoader createLoader(ApplicationContext context) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return context.getBean(param);
            }
        });
        return loader;
    }


}
