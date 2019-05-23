package ec.mark.im.gencode;

import com.jfinal.kit.PathKit;

import io.jboot.codegen.model.JbootBaseModelGenerator;
import io.jboot.codegen.model.JbootModelGenerator;

public class GenCode {
	public static void main(String args[]) {
		String baseModelPackage = "ec.mark.im.common.base";
		String modelPackage = "ec.mark.im.common.model";
		String modelDir = PathKit.getWebRootPath() + "/src/main/java/ec/mark/im/common/model";
		String baseDir = PathKit.getWebRootPath() + "/src/main/java/ec/mark/im/common/base";
		JbootBaseModelGenerator bgen = new JbootBaseModelGenerator(baseModelPackage, baseDir);
		bgen.generate();
		JbootModelGenerator gen = new JbootModelGenerator(modelPackage, baseModelPackage, modelDir);
		gen.generate();
	}
}
