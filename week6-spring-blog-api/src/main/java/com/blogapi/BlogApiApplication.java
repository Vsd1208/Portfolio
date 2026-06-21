package com.blogapi;

import com.blogapi.model.entity.Category;
import com.blogapi.model.entity.Comment;
import com.blogapi.model.entity.Post;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BlogApiApplication {
    private static final Logger logger = LoggerFactory.getLogger(BlogApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BlogApiApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(CategoryRepository categoryRepository,
                               PostRepository postRepository,
                               CommentRepository commentRepository) {
        return args -> {
            if (categoryRepository.count() > 0) {
                return;
            }

            Category technology = categoryRepository.save(new Category("Technology", "Tools and trends in software"));
            Category programming = categoryRepository.save(new Category("Programming", "Practical programming tutorials"));
            Category web = categoryRepository.save(new Category("Web Development", "Frontend and backend web topics"));

            Post springPost = postRepository.save(new Post(
                    "Getting Started with Spring Boot",
                    "Spring Boot makes Java web applications faster to configure and easier to ship.",
                    "Aarav Mehta",
                    programming
            ));
            Post restPost = postRepository.save(new Post(
                    "Designing Clean REST APIs",
                    "Consistent resources, validation, and status codes make APIs easier to consume.",
                    "Nisha Rao",
                    web
            ));
            postRepository.save(new Post(
                    "Why H2 Is Useful for Development",
                    "An in-memory database helps teams test API flows without a local database server.",
                    "Kabir Singh",
                    technology
            ));

            commentRepository.saveAll(List.of(
                    new Comment("Very clear introduction.", "Priya", springPost),
                    new Comment("The endpoint examples helped a lot.", "Dev", restPost)
            ));

            logger.info("Sample blog data loaded");
        };
    }
}
