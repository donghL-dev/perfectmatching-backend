package com.matching.controller;

import com.matching.domain.Comment;
import com.matching.domain.Project;
import com.matching.domain.User;
import com.matching.domain.enums.LocationType;
import com.matching.domain.enums.ProjectStatus;
import com.matching.repository.CommentRepository;
import com.matching.repository.ProjectRepository;
import com.matching.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Comment comment;

    @Before
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(print())
                .build();

        User user = User.builder().nick("Test User").email("Test_User@gmail.com").password("test_password")
                .profileImg("image..").description("test desc..").createdDate(LocalDateTime.now())
                .investTime(4).socialUrl("https://github.com/testUser").build();

        userRepository.save(user) ;

        Project project = Project.builder().leader(user).title("테스트 프로젝트").content("테스트 생성").summary("테스트 프로젝트")
                .status(ProjectStatus.getRandomProjectStatus()).location(LocationType.getRandomLocationType())
                .createdDate(LocalDateTime.now()).designerRecruits(1).developerRecruits(1).etcRecruits(1).marketerRecruits(1).plannerRecruits(1)
                .socialUrl("https://github.com/testUser/testProject").build();

        projectRepository.save(project);


        comment = Comment.builder().project(project).writer(user).content("테스트 댓글").createdDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    @Test
    public void getCommentTest() throws Exception {
        mockMvc.perform(get("/api/project/" + comment.getIdx()).with(user("Test_User@gmail.com")
                .password("test_password")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(redirectedUrl("/api/project/" + comment.getIdx()))
                .andExpect(status().isOk());
    }

}