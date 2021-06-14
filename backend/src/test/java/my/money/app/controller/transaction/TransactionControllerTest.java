package my.money.app.controller.transaction;

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
public class TransactionControllerTest extends BaseControllerTest {

    private final String PATH = "/transactions";

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

        this.createTransaction();

    }

    @Test
    public void updateWithSuccess() throws Exception {
        Map<String, String> createdTransaction = this.createTransaction();
        String token = createdTransaction.get("token");
        String id = createdTransaction.get("id");

        Map<String, String> data = new HashMap<>();
        data.put("value", "15.20");
        data.put("type", "Despesa");
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
        Map<String, String> createdTransaction = this.createTransaction();
        String token = createdTransaction.get("token");
        String id = createdTransaction.get("id");

        this.mockMvc
                .perform(
                        delete(PATH + "/" + id)
                                .header("x-authorization", token)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    public Map<String, String> createTransaction() throws Exception {
        String token = this.createToken();

        Map<String, String> data = new HashMap<>();
        data.put("value", "100.20");
        data.put("type", "Despesa");
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