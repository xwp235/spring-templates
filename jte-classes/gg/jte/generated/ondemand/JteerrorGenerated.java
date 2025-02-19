package gg.jte.generated.ondemand;
import java.util.Map;
import java.util.Objects;
@SuppressWarnings("unchecked")
public final class JteerrorGenerated {
	public static final String JTE_NAME = "error.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,8,8,8,8,9,9,9,11,11,12,12,13,13,13,13,13,13,14,14,15,15,17,17,17,2,3,4,4,4,4};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JteerrorGenerated.class, "JteerrorGenerated.bin", 25,2,4,2,10,2,7,2,4);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	private static final byte[] TEXT_PART_BINARY_3 = BINARY_CONTENT.get(3);
	private static final byte[] TEXT_PART_BINARY_4 = BINARY_CONTENT.get(4);
	private static final byte[] TEXT_PART_BINARY_5 = BINARY_CONTENT.get(5);
	private static final byte[] TEXT_PART_BINARY_6 = BINARY_CONTENT.get(6);
	private static final byte[] TEXT_PART_BINARY_7 = BINARY_CONTENT.get(7);
	private static final byte[] TEXT_PART_BINARY_8 = BINARY_CONTENT.get(8);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String message, Integer status, Map<String,Object> errors) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("html", null);
		jteOutput.writeUserContent(message);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("html", null);
		jteOutput.writeUserContent(status);
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
		if (Objects.nonNull(errors) && !errors.isEmpty()) {
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_3);
			for (Map.Entry<String,Object> entry : errors.entrySet()) {
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_4);
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(entry.getKey());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_5);
				jteOutput.setContext("li", null);
				jteOutput.writeUserContent(entry.getValue().toString());
				jteOutput.writeBinaryContent(TEXT_PART_BINARY_6);
			}
			jteOutput.writeBinaryContent(TEXT_PART_BINARY_7);
		}
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_8);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String message = (String)params.get("message");
		Integer status = (Integer)params.get("status");
		Map<String,Object> errors = (Map<String,Object>)params.get("errors");
		render(jteOutput, jteHtmlInterceptor, message, status, errors);
	}
}
