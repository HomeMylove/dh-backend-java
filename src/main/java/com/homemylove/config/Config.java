package com.homemylove.config;

import com.homemylove.intercept.LoginInterceptor;
import com.homemylove.properties.JwtProperties;
import com.homemylove.properties.OssProperties;
import com.homemylove.properties.ResourceProperties;
import com.homemylove.service.ArticleService;
import com.homemylove.service.AuthorService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({
        ResourceProperties.class,
        JwtProperties.class,
        OssProperties.class
})
@EnableScheduling
public class Config implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private AuthorService authorService;

    @Resource
    private ArticleService articleService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/register","/api/user/register","/src/**")
                .order(1);
    }

    // 定时更新热搜
    @Scheduled(cron = "0 0 * * * *")    // 每小时
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
//    @Scheduled(fixedRate = 5000)      // 五秒
    public void updatePopularity(){
        System.out.println("update");
        authorService.updateAuthorPopularity();
        articleService.updateArticlePopularity();
    }



}





