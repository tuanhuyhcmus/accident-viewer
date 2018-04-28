/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.syncdblocal.common.JsonParserUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import thesis.api.common.CommonModel;
import thesis.api.common.JsonUtil;
import thesis.api.data.Schedule;
import thesis.api.model.ScheduleModel;

/**
 *
 * @author huynct
 */
public class ScheduleController extends HttpServlet {

    protected final Logger logger = Logger.getLogger(this.getClass());
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();

        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String uid = req.getParameter("uid") != null ? req.getParameter("uid") : "";
        String content = "";

        pathInfo = pathInfo.toLowerCase();
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);

        logger.info("ScheduleController.processs: cmd = " + cmd + ", data = " + data);

        JsonObject jsonData = JsonParserUtil.parseJsonObject(data);

        if (jsonData == null) {
            content = CommonModel.FormatResponse(-1, "Invalid json data");
            logger.error(getClass().getSimpleName() + ".processs: " + "Invalid json data");
            CommonModel.out(content, resp);
            return;
        }

        switch (cmd) {
            case "insert_schedule":
                content = insertSchedule(jsonData);
                break;
            case "update_schedule":
                content = updateSchedule(jsonData);
                break;
            case "get_list_schedule":
                content = getListSchedule(uid, jsonData);
                break;
            default:
                content = CommonModel.FormatResponse(-1, " Cmd không hợp lệ ");
                break;
        }
        logger.info(getClass().getSimpleName() + ".processs: response = " + content);

        CommonModel.out(content, resp);
    }

    private String insertSchedule(JsonObject jsonData) {

        String content = "";

        Schedule schedule = new Schedule();
        schedule = JsonUtil.fromJson(jsonData, Schedule.class);

        int ret = -1;

        try {
            ret = ScheduleModel.getInstance().insertSchedule(schedule);
            if (ret == 0) {
                content = CommonModel.FormatResponse(0, "thanh cong roi nhe", JsonUtil.toJsonTree(schedule));
            } else {
                content = CommonModel.FormatResponse(-1, "insert schedule that bai");
            }
        } catch (IOException ex) {
            logger.info(getClass().getSimpleName() + ".insertSchedule: " + ex.getMessage());
            content = CommonModel.FormatResponse(-1, "insert schedule that bai");

        }

        return content;
    }

    private String getListSchedule(String uid, JsonObject jsonData) {

        String content = "";
        int ret = -1;
        try {
            List<Schedule> listSchedule = new ArrayList<>();
            ret = ScheduleModel.getInstance().getListSchedule(uid, listSchedule);
            if (ret == 0) {
                content = CommonModel.FormatResponse(0, "thanh cong roi nhe", JsonUtil.toJsonTree(listSchedule));
            } else {
                content = CommonModel.FormatResponse(-1, "insert schedule that bai");
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
            content = CommonModel.FormatResponse(-1, "insert schedule that bai");

        }

        return content;
    }

    private String updateSchedule( JsonObject jsonData) {
        String content = "";

        Schedule schedule = JsonUtil.fromJson(jsonData, Schedule.class);

        int ret = -1;

        try {
            ret = ScheduleModel.getInstance().updateSchedule(schedule);
            if (ret == 0) {
                content = CommonModel.FormatResponse(0, "thanh cong roi nhe", JsonUtil.toJsonTree(schedule));
            } else {
                content = CommonModel.FormatResponse(-1, "updateSchedule schedule that bai");
            }
        } catch (IOException ex) {
            logger.info(getClass().getSimpleName() + ".updateSchedule: " + ex.getMessage());
            content = CommonModel.FormatResponse(-1, "updateSchedule schedule that bai");

        }

        return content;
    }

}
