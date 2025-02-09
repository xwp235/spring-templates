package jp.onehr.base.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jp.onehr.base.common.constants.StatusCodeConstants;
import jp.onehr.base.common.exceptions.AppException;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class JsonUtil {

    private static ObjectMapper mapper;

    /**
     * <p>将Java对象序列化成JSON字符串</p>
     */
    public static String obj2Json(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return getMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCodeConstants.OBJ_SERIALIZE_2_JSON_FAILED, SpringUtil.getMessage("objSerialize2JsonFailed"),e);
        }
    }

    /**
     * <p>将Java对象序列化JSON字符串，可以自定义包含属性，黙认配置在application.yml文件中，
     * 因为这里的ObjectMapper与Spring框架共用，因些在设置完属性后，要还原成原来的包含属性。</p>
     * <p>不推荐使用此方法，因为每次都会创建新的objectMapper，会有一定的性能问题。</p>
     * <p>因为原对象在使用一次后会缓存序列化属性，导致第二次设置的属性无效</p>
     */
    public static String obj2Json(Object object, JsonInclude.Include include) {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanUtils.copyProperties(getMapper(), objectMapper);
        objectMapper.setSerializationInclusion(include);
        try {
            return getMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCodeConstants.OBJ_SERIALIZE_2_JSON_FAILED, SpringUtil.getMessage("objSerialize2JsonFailed"),e);
        }
    }

    /**
     * <p>将json字符串反序列化成Java对象</p>
     */
    public static <T> T json2Obj(String json, Class<T> type) {
        try{
            return getMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCodeConstants.JSON_DESERIALIZE_2_OBJ_FAILED,SpringUtil.getMessage("jsonDeserialize2objFailed"),e);
        }
    }

    /**
     * <p>将json字符串反序列化成Java对象，
     * 涉及到数组泛型类型的转化，如果调用上述方法出现LinkedHashMap转换报错，
     * 可以使用这个方法</p>
     */
    public static <T> T json2Obj(String json, TypeReference<T> valueTypeRef) {
        try{
            return getMapper().readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCodeConstants.JSON_DESERIALIZE_2_OBJ_FAILED,SpringUtil.getMessage("jsonDeserialize2objFailed"),e);
        }
    }

    /**
     * 获取Jackson ObjectMapper对象
     */
    private static ObjectMapper getMapper() {
        if (Objects.nonNull(mapper)) {
            return mapper;
        }
        // 复用Spring的ObjectMapper以获更高的性能
        mapper = SpringUtil.getBean(ObjectMapper.class);
        return mapper;
    }

    // 合并单个 JSON 对象
    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        if (mainNode !=null && mainNode.isObject() && updateNode!=null && updateNode.isObject()) {
            var mainObject = (ObjectNode) mainNode;
            var updateObject = (ObjectNode) updateNode;
            updateObject.fields().forEachRemaining(entry -> {
                var jsonNode = mainObject.get(entry.getKey());
                // If the field exists and is an object, recursively merge
                if (jsonNode != null && jsonNode.isObject()) {
                    merge(jsonNode, entry.getValue());
                } else {
                    mainObject.set(entry.getKey(), entry.getValue());
                }
            });
        }
        return mainNode;
    }

    // 合并可变参数 JSON 对象
    public static JsonNode mergeMultiple(JsonNode... jsonNodes) {
        if (jsonNodes == null || jsonNodes.length == 0) {
            return getMapper().createObjectNode();
        }
        var result = deepCopy(jsonNodes[0]);
        for (var i = 1; i < jsonNodes.length; i++) {
            if (result != null) {
                merge(result, jsonNodes[i]);
            }
        }
        return result;
    }

    private static JsonNode deepCopy(JsonNode original) {
        try {
            return getMapper().readTree(getMapper().writeValueAsString(original));
        } catch (JsonProcessingException e) {
            throw new AppException(StatusCodeConstants.DEEP_COPY_OBJ_FAILED,SpringUtil.getMessage("deepCopyObjFailed"),e);
        }
    }

}
