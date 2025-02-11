package gg.jte.generated.ondemand;
import jp.onehr.base.controller.DemoModel;
@SuppressWarnings("unchecked")
public final class JtedemoGenerated {
	public static final String JTE_NAME = "demo.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,2,2,2,2,4,4,4,4,7,7,7,8,8,8,2,2,2,2};
	private static final gg.jte.runtime.BinaryContent BINARY_CONTENT = gg.jte.runtime.BinaryContent.load(JtedemoGenerated.class, "JtedemoGenerated.bin", 8,38,3);
	private static final byte[] TEXT_PART_BINARY_0 = BINARY_CONTENT.get(0);
	private static final byte[] TEXT_PART_BINARY_1 = BINARY_CONTENT.get(1);
	private static final byte[] TEXT_PART_BINARY_2 = BINARY_CONTENT.get(2);
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, DemoModel model) {
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_0);
		jteOutput.setContext("html", null);
		jteOutput.writeUserContent(model.getName());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_1);
		jteOutput.setContext("html", null);
		jteOutput.writeUserContent(model.getVisits());
		jteOutput.writeBinaryContent(TEXT_PART_BINARY_2);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		DemoModel model = (DemoModel)params.get("model");
		render(jteOutput, jteHtmlInterceptor, model);
	}
}
