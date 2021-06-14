package my.money.app.controller.goals;

import com.google.gson.Gson;
import my.money.app.BaseControllerTest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class GoalsControllerTest extends BaseControllerTest {

    private final String PATH = "/goals";

    @Test
    public void listWithSuccess() throws Exception {

        String token = this.createToken();

        this.mockMvc
                .perform(
                        get(PATH)
                                .header("x-authorization", token)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void createWithSuccess() throws Exception {

        this.createGoal();

    }

    @Test
    public void updateWithSuccess() throws Exception {
        Map<String, String> createdGoal = this.createGoal();
        String token = createdGoal.get("token");
        String id = createdGoal.get("id");

        Map<String, String> data = new HashMap<>();
        data.put("value", "15.20");
        data.put("description", "viagem de ano novo");
        data.put("data", "2020-12-19");

        String jsonContent = JSONObject.toJSONString(data);

        this.mockMvc
                .perform(
                        put(PATH + "/" + id)
                                .header("x-authorization", token)
                                .content(jsonContent)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void deleteWithSuccess() throws Exception {
        Map<String, String> createdGoal = this.createGoal();
        String token = createdGoal.get("token");
        String id = createdGoal.get("id");

        this.mockMvc
                .perform(
                        delete(PATH + "/" + id)
                                .header("x-authorization", token)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    public Map<String, String> createGoal() throws Exception {
        String token = this.createToken();

        Map<String, String> data = new HashMap<>();
        data.put("value", "100.20");
        data.put("description", "viagem de ano novo");
        data.put("data", "2020-12-19");

        String jsonContent = JSONObject.toJSONString(data);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        post(PATH)
                                .header("x-authorization", token)
                                .content(jsonContent)
                                .characterEncoding("utf-8")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();

        HashMap<String, String> map = new Gson().fromJson(
                mvcResult.getResponse().getContentAsString(),
                HashMap.class
        );

        map.put("token", token);

        return map;
    }
}