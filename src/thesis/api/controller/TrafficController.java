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
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import thesis.api.common.AppConst;
import thesis.api.common.CommonModel;
import thesis.api.common.JsonUtil;
import thesis.api.data.History;
import thesis.api.model.AccidentModel;
import thesis.api.model.HistoryModel;
import thesis.api.model.MiddleModel;
import thesis.api.model.SessionModel;
import thesis.api.model.UserModel;

/**
 *
 * @author huynct
 */
public class TrafficController extends HttpServlet {

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
        String content = "";

        pathInfo = pathInfo.toLowerCase();
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        logger.info("TrafficController.processs: cmd = " + cmd + ", data = " + data);

        JsonObject jsonData = JsonParserUtil.parseJsonObject(data);

        if (jsonData == null) {
            content = CommonModel.FormatResponse(-1, "Invalid json data");
            logger.error(getClass().getSimpleName() + ".processs: " + "Invalid json data");
            CommonModel.out(content, resp);
            return;
        }

        switch (cmd) {
            case "insert_traffic":
                content = insertTraffic(jsonData);
                break;
            case "get_all_active_traffic":
                content = getAllActiveTraffic(jsonData);
                break;
            default:
                content = CommonModel.FormatResponse(-1, " Cmd không hợp lệ ");
                break;
        }
        logger.info(getClass().getSimpleName() + ".processs: response = " + content);

        CommonModel.out(content, resp);
    }

    private String insertTraffic(JsonObject jsonData) {
        String content = "";
        int ret = -1;
        History history = JsonUtil.fromJson(jsonData, History.class);
        AtomicLong existedHistoryId = new AtomicLong(0);
        try {

            //kiem tra xem accident có đủ điều kiện để vô bảng history hay ko
            ret = UserModel.getInstance().checkIsTrustyUser(history.getUserId());
            switch (ret) {
                case 0:
                    //kiểm tra xem vụ tai nạn đã có trong bảng session hay chưa
                    ret = MiddleModel.getInstance().checkIsActiveAccident(history, existedHistoryId,AppConst.TYPE_TRAFFIC);
                    if (ret == 0)//accident hiện chưa có trong bảng session
                    {
                        ret = HistoryModel.getInstance().insertHistory(history);
                        if (ret == 0) {
                            ret = SessionModel.getInstance().insertSession(history);
                            if (ret == 0) {
                                content = CommonModel.FormatResponse(0, "thanh cong roi nhe", JsonUtil.toJsonTree(history));
                            }
                        } else {
                            content = CommonModel.FormatResponse(-1, "insert Traffic that bai");
                        }
                    } else if (ret == 1) {//merge thông tin với history cũ
                        History existedHistory = new History();
                        ret = HistoryModel.getInstance().mergeInfoWithExistedHistory(existedHistoryId, history, existedHistory);
                        if (ret == 0) {
                            ret = SessionModel.getInstance().updateSession(existedHistory);
                            if (ret == 0) {
                                content = CommonModel.FormatResponse(1, "Traffic đã được report trước đó bỏi người khác, thông tin bạn"
                                        + " vừa report đã được merge với vụ traffic cũ", existedHistory);
                            }
                        } else {
                            content = CommonModel.FormatResponse(-1, "insert Traffic that bai");
                        }
                    }
                    break;
                case 1:
                    content = CommonModel.FormatResponse(-1, "tài khoản không đủ trusty point (vui lòng liên hệ 01627912324 để được giải đáp)");
                    break;
                default:
                    content = CommonModel.FormatResponse(-1, "insert Traffic that bai");
                    break;
            }
        } catch (IOException ex) {
            logger.info(getClass().getSimpleName() + ".insertTraffic: " + ex.getMessage());
            content = CommonModel.FormatResponse(-1, "insert Traffic that bai");

        }
        return content;
    }

    private String getAllActiveTraffic(JsonObject jsonData) {
        String content = "";

        try {
            int ret = -1;
            List<History> listActiveTraffic = new ArrayList<>();
            ret = AccidentModel.getInstance().getAllActiveTraffic(listActiveTraffic);
            if (ret == 0) {

                content = CommonModel.FormatResponse(0, "Lấy danh sách traffic thành công", JsonUtil.toJsonTree(listActiveTraffic));
            } else {
                content = CommonModel.FormatResponse(-1, "lấy danh sách thất bại");

            }
        } catch (IOException ex) {
            logger.info(getClass().getSimpleName() + ".getAllActiveTraffic: " + ex.getMessage());
            content = CommonModel.FormatResponse(-1, "getAllActiveTraffic that bai");

        }
        return content;
    }
}
