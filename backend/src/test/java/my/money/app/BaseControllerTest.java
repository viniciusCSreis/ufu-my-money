package my.money.app;

import com.google.gson.Gson;
import my.money.app.entity.User;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    public
    MockMvc mockMvc;


    public String createToken() throws Exception {

        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();

        Map<String, String> data = new HashMap<>();
        data.put("cpf", UUID.randomUUID().toString());
        data.put("username", username);
        data.put("email", UUID.randomUUID().toString() + "@gmail.com");
        data.put("password", password);

        String jsonContent = JSONObject.toJSONString(data);

        this.mockMvc
                .perform(
                        post("/sign-up")
                                .content(jsonContent)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated());

        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        String loginJsonContent = JSONObject.toJSONString(loginData);
        MvcResult mvcResult = this.mockMvc
                .perform(
                        post("/sign-in")
                                .content(loginJsonContent)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        HashMap<String, String> map = new Gson().fromJson(
                mvcResult.getResponse().getContentAsString(),
                HashMap.class
        );

        return map.get("token");
    }
}