package com.wufu.producer;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.junit.Test;

public class TestMybatisGenerator {

    /**
     * 自动生成 user 相关的 java 文件
     */
    @Test
    public void autoGenerate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/producer?useUnicode=true&characterEncoding=utf8",
                "root", "root")
                .globalConfig(
                        builder -> builder.author("wufu")
                            .outputDir("C:\\Users\\wufu\\IdeaProjects\\spring-cloud-learn\\producer_8000\\src\\main\\java")
                            .build()
                )
                .packageConfig(
                        builder -> builder.parent("com.wufu.producer")
                                .build())
                .strategyConfig(
                        // 数据库表名
                        builder -> builder.addInclude("producer_user").entityBuilder().enableLombok()
                )
                .execute();
    }
}
