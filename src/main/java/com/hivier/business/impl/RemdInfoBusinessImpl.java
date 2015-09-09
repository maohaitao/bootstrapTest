package com.hivier.business.impl;

import com.hivier.business.RemdInfoBusiness;
import com.hivier.cache.RedisCacheService;
import com.hivier.cache.RedisService;
import com.hivier.model.HivierProductInfo;
import com.hivier.model.DiaryItemItem;
import com.hivier.model.DiarySrcImage;
import com.hivier.model.DiarySrcItem;
import com.hivier.service.impl.HivierProductInfoServiceImpl;
import com.hivier.service.impl.DiaryItemItemServiceImpl;
import com.hivier.service.impl.DiarySrcImageServiceImpl;
import com.hivier.service.impl.DiarySrcItemServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sf.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hesin
 * @Created with： com.pc.buyer.business.impl
 * @Des: 首页业务处理
 * @date 2015/8/21
 */
@Service
public class RemdInfoBusinessImpl implements RemdInfoBusiness {

    private Integer size = 20;
    private String SRC_REDIS_KEY="src_";

    @Autowired
    private DiarySrcItemServiceImpl pcSrcItemService;

    @Autowired
    private DiarySrcImageServiceImpl pcSrcImageService;

    @Autowired
    private HivierProductInfoServiceImpl imageInfoService;

    @Autowired
    private DiaryItemItemServiceImpl pcItemItemService;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    //区分banner
    @Override
    public JsonObject remdInfo(String typeid, String mark, Integer must, Integer pageid) {
        JsonObject jsonObject = new JsonObject();
        DiarySrcItem srcItem = null;
        if (typeid != null && CommonUtil.isNumber(typeid)) {
            srcItem = pcSrcItemService.getByTypeId(Integer.parseInt(typeid));
        }
        if (srcItem != null) {
            jsonObject = getChildSrcItem(srcItem, pageid);
        }
        return jsonObject;
    }

    /**
     * 获取子节点数据
     * @param srcItem
     * @return
     */
    public JsonObject getChildSrcItem(DiarySrcItem srcItem, Integer pageid) {
        JsonObject jsonObject = new JsonObject();
        if (srcItem != null) {
            if (srcItem.getDatatype() == 1) { //对应分类项
                jsonObject = (JsonObject) RedisService.get(SRC_REDIS_KEY + RedisCacheService.modekey + srcItem.getId(),null);
//                List<PcItemItem> list = childItem(srcItem.getId());
//                childSrcItem(srcItem, jsonObject, list, srcItem.getTypeId());
            } else if (srcItem.getDatatype() == 2) { //对应分类列表
                jsonObject = (JsonObject) RedisService.get(srcItem.getId() + RedisCacheService.modekey +pageid,null);
//                int re = pcSrcImageService.getCount();
//                for (int i = 0; i < re; i += size) {
//                    List<PcSrcImage> childType = childType(srcItem.getId(), i);
//                    JsonObject rejson = childSrcType(srcItem, jsonObject, childType);
//                    RedisService.set(srcItem.getId() + RedisCacheService.modekey + i, rejson, 0);
//                }
            }
        }
        return jsonObject;
    }


    @Override
    public boolean initRemdInfo() {
        List<DiarySrcItem> list = pcSrcItemService.listAllParent();
        if (list != null && list.size() > 0) {
            for (DiarySrcItem item : list) {
                JsonObject jsonObject = new JsonObject();
                childSrcItem(item,jsonObject);
            }
        }
        return false;
    }


    public void childSrcItem(DiarySrcItem srcItem, JsonObject jsonObject) {
        if (srcItem != null) {
            if (srcItem.getDatatype() == 1) { //对应分类项
                List<DiaryItemItem> list = childItem(srcItem.getId());
                childSrcItem(srcItem, jsonObject, list, srcItem.getId());
                RedisService.set(SRC_REDIS_KEY + RedisCacheService.modekey + srcItem.getId(), jsonObject, 0);
            } else if (srcItem.getDatatype() == 2) { //对应分类列表
                int re = pcSrcImageService.getSrcImageCount();
//                System.out.println("re=="+re);
                int pagenum = re%size==0?re/size:re/size+1;
                for (int i = 1; i <=pagenum; i++) {
                    List<DiarySrcImage> childType = childType(srcItem.getId(), i);
                    JsonObject rejson = childSrcType(srcItem, jsonObject, childType);
                    RedisService.set(srcItem.getId() + RedisCacheService.modekey + i, rejson, 0);
                }
            }
        }
    }

    public JsonObject childSrcItem(DiarySrcItem srcItem, JsonObject jsonObject, List<DiaryItemItem> list, Integer id) {
        if (srcItem == null) {
            return jsonObject;
        }
        JsonObject jsonElement = gson.toJsonTree(srcItem).getAsJsonObject();
        JsonArray array = new JsonArray();
        if (list != null && list.size() > 0) {
            for (DiaryItemItem itemItem : list) {
                if (itemItem != null) {
                    DiarySrcItem item = pcSrcItemService.getById(itemItem.getChildid());
                    if (item != null) {
                        childSrcItem(item, jsonObject);
                        JsonObject json = new JsonObject();
                        json.addProperty("name", item.getTypeName());
                        json.addProperty("seq", item.getSeq());
                        json.addProperty("cicon", item.getCicon());
                        json.addProperty("icon", item.getIcon());
                        json.addProperty("moreid", item.getMoreid() == 1 ? true : false);
                        json.addProperty("rid", item.getId());
                        json.addProperty("showtitle", item.getShowtitle() == 1 ? true : false);
                        array.add(json);
                    }
                }
            }
        }
        jsonElement.add("typedata", array);
        jsonObject.add(id != null ? id + "" : srcItem.getId() + "", jsonElement);
        return jsonObject;
    }

    public JsonObject childSrcType(DiarySrcItem srcItem, JsonObject jsonObject, List<DiarySrcImage> list) {
        if (srcItem == null) {
            return null;
        }
        JsonObject jsonElement = gson.toJsonTree(srcItem).getAsJsonObject();
        JsonArray array = new JsonArray();
        if (list != null && list.size() > 0) {
            for (DiarySrcImage pcSrcImage : list) {
                if (pcSrcImage != null) {
                    HivierProductInfo item = imageInfoService.getImageById(pcSrcImage.getImageId());
                    if (item != null) {
                        JsonObject json = new JsonObject();
                        json.addProperty("thumb", item.getThumbIcon());
                        json.addProperty("icon", item.getIcon());
                        json.addProperty("address", item.getAddress());
                        json.addProperty("story", item.getStory());
                        json.addProperty("rid", item.getId());
                        json.addProperty("onlinetime", pcSrcImage.getOnlineTime());
                        json.addProperty("priority", pcSrcImage.getPriority());
//                        json.addProperty("acttype", pcSrcImage.getActtype());
//                        json.addProperty("actvalue", pcSrcImage.getActvalue());
                        array.add(json);
                    }
                }
            }
        }
        jsonElement.add("resdata", array);
        jsonObject.add(srcItem.getId() + "", jsonElement);
        return jsonObject;
    }

    /**
     * 获取子节点对应的分类项目
     *
     * @param itemid
     * @return
     */
    public List<DiaryItemItem> childItem(Integer itemid) {
        if (itemid == null) {
            return null;
        }
        List<DiaryItemItem> childItems = pcItemItemService.getParentId(itemid);
        return childItems;
    }

    //分页处理子项的应用
    public List<DiarySrcImage> childType(Integer itemid, Integer pageid) {
        if (itemid == null) {
            return null;
        }
        List<DiarySrcImage> childType = pcSrcImageService.getParentId(itemid, pageid);
        return childType;
    }
}
