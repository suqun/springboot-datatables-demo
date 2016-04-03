package com.larry.web;

import com.alibaba.fastjson.JSON;
import com.larry.model.App;
import com.larry.model.AppSpec;
import com.larry.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AppController {

    @Autowired
    private AppRepository appRepository;
    /**
     * 查询
     *
     * @param input DataTablesInput
     * @return DataTablesOutput<App>
     */
    @ResponseBody
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
        try {
            return appRepository.findAll(input, AppSpec.isNotDelete());
        } catch (Exception e) {
            return null;
        }
//        return appRepository.findAll(input);
    }

    /**
     * 初始化数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String initData() {

        Map<String, String> map = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            App app = new App();
            app.setUrl("http://suqun.github.io/archives/");
            app.setKeywords("key" + i);
            app.setDescription("spring data" + i);
            app.setName("app" + i);
            app.setHot(i);
            app.setDisabled(true);
            appRepository.save(app);
        }

        map.put("msg", "success");
        return JSON.toJSONString(map);
    }

}
