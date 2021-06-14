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

import java.util.*;

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
        data.put("cpf", this.createCPF());
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

    private String createCPF() {
        int n = 9;
        int n1 = (int) (Math.random() * n);
        int n2 = (int) (Math.random() * n);
        int n3 = (int) (Math.random() * n);
        int n4 = (int) (Math.random() * n);
        int n5 = (int) (Math.random() * n);
        int n6 = (int) (Math.random() * n);
        int n7 = (int) (Math.random() * n);
        int n8 = (int) (Math.random() * n);
        int n9 = (int) (Math.random() * n);
        int d1 = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;

        d1 = 11 - (d1 % 11);
        if (d1 >= 10)
            d1 = 0;

        int d2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10)
            d2 = 0;

        return "" + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + d1 + d2;
    }
}