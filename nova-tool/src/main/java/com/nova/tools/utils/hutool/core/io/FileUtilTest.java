package com.nova.tools.utils.hutool.core.io;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.LineSeparator;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.lang.Assert;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * {@link FileUtil} 单元测试类
 *
 * @author Looly
 */
public class FileUtilTest {

	@Test
	public void fileTest() {
		File file = FileUtil.file("d:/aaa", "bbb");
		Assert.notNull(file);

		// 构建目录中出现非子目录抛出异常
		FileUtil.file(file, "../ccc");

		FileUtil.file("E:/");
	}

	@Test
	public void getAbsolutePathTest() {
		String absolutePath = FileUtil.getAbsolutePath("LICENSE-junit.txt");
		Assert.notNull(absolutePath);
		String absolutePath2 = FileUtil.getAbsolutePath(absolutePath);
		Assert.notNull(absolutePath2);
		Assert.equals(absolutePath, absolutePath2);

		String path = FileUtil.getAbsolutePath("中文.xml");
		Assert.isTrue(path.contains("中文.xml"));

		path = FileUtil.getAbsolutePath("d:");
		Assert.equals("d:", path);
	}

	@Test
	@Ignore
	public void touchTest() {
		FileUtil.touch("d:\\tea\\a.jpg");
	}

	@Test
	@Ignore
	public void delTest() {
		// 删除一个不存在的文件，应返回true
		boolean result = FileUtil.del("e:/Hutool_test_3434543533409843.txt");
		Assert.isTrue(result);
	}

	@Test
	@Ignore
	public void delTest2() {
		// 删除一个不存在的文件，应返回true
		boolean result = FileUtil.del(Paths.get("e:/Hutool_test_3434543533409843.txt"));
		Assert.isTrue(result);
	}

	@Test
	@Ignore
	public void renameTest() {
		FileUtil.rename(FileUtil.file("d:/test/3.jpg"), "2.jpg", false);
	}

	@Test
	@Ignore
	public void renameTest2() {
		FileUtil.move(FileUtil.file("d:/test/a"), FileUtil.file("d:/test/b"), false);
	}

	@Test
	public void copyTest() {
		File srcFile = FileUtil.file("hutool.jpg");
		File destFile = FileUtil.file("hutool.copy.jpg");

		FileUtil.copy(srcFile, destFile, true);

		Assert.isTrue(destFile.exists());
		Assert.equals(srcFile.length(), destFile.length());
	}

	@Test
	@Ignore
	public void copyFilesFromDirTest() {
		File srcFile = FileUtil.file("D:\\驱动");
		File destFile = FileUtil.file("d:\\驱动备份");

		FileUtil.copyFilesFromDir(srcFile, destFile, true);
	}

	@Test
	@Ignore
	public void copyDirTest() {
		File srcFile = FileUtil.file("D:\\test");
		File destFile = FileUtil.file("E:\\");

		FileUtil.copy(srcFile, destFile, true);
	}

	@Test
	@Ignore
	public void moveDirTest() {
		File srcFile = FileUtil.file("E:\\test2");
		File destFile = FileUtil.file("D:\\");

		FileUtil.move(srcFile, destFile, true);
	}

	@Test
	public void equalsTest() {
		// 源文件和目标文件都不存在
		File srcFile = FileUtil.file("d:/hutool.jpg");
		File destFile = FileUtil.file("d:/hutool.jpg");

		boolean equals = FileUtil.equals(srcFile, destFile);
		Assert.isTrue(equals);

		// 源文件存在，目标文件不存在
		File srcFile1 = FileUtil.file("hutool.jpg");
		File destFile1 = FileUtil.file("d:/hutool.jpg");

		boolean notEquals = FileUtil.equals(srcFile1, destFile1);
		Assert.isFalse(notEquals);
	}

	@Test
	@Ignore
	public void convertLineSeparatorTest() {
		FileUtil.convertLineSeparator(FileUtil.file("d:/aaa.txt"), CharsetUtil.CHARSET_UTF_8, LineSeparator.WINDOWS);
	}

	@Test
	public void normalizeTest() {
		Assert.equals("/foo/", FileUtil.normalize("/foo//"));
		Assert.equals("/foo/", FileUtil.normalize("/foo/./"));
		Assert.equals("/bar", FileUtil.normalize("/foo/../bar"));
		Assert.equals("/bar/", FileUtil.normalize("/foo/../bar/"));
		Assert.equals("/baz", FileUtil.normalize("/foo/../bar/../baz"));
		Assert.equals("/", FileUtil.normalize("/../"));
		Assert.equals("foo", FileUtil.normalize("foo/bar/.."));
		Assert.equals("../bar", FileUtil.normalize("foo/../../bar"));
		Assert.equals("bar", FileUtil.normalize("foo/../bar"));
		Assert.equals("/server/bar", FileUtil.normalize("//server/foo/../bar"));
		Assert.equals("/bar", FileUtil.normalize("//server/../bar"));
		Assert.equals("C:/bar", FileUtil.normalize("C:\\foo\\..\\bar"));
		//
		Assert.equals("C:/bar", FileUtil.normalize("C:\\..\\bar"));
		Assert.equals("../../bar", FileUtil.normalize("../../bar"));
		Assert.equals("C:/bar", FileUtil.normalize("/C:/bar"));
		Assert.equals("C:", FileUtil.normalize("C:"));
		Assert.equals("\\/192.168.1.1/Share/", FileUtil.normalize("\\\\192.168.1.1\\Share\\"));
	}

	@Test
	public void normalizeBlankTest() {
		Assert.equals("C:/aaa ", FileUtil.normalize("C:\\aaa "));
	}

	@Test
	public void normalizeHomePathTest() {
		String home = FileUtil.getUserHomePath().replace('\\', '/');
		Assert.equals(home + "/bar/", FileUtil.normalize("~/foo/../bar/"));
	}

	@Test
	public void normalizeHomePathTest2() {
		String home = FileUtil.getUserHomePath().replace('\\', '/');
		// 多个~应该只替换开头的
		Assert.equals(home + "/~bar/", FileUtil.normalize("~/foo/../~bar/"));
	}

	@Test
	public void normalizeClassPathTest() {
		Assert.equals("", FileUtil.normalize("classpath:"));
	}

	@Test
	public void normalizeClassPathTest2() {
		Assert.equals("../a/b.csv", FileUtil.normalize("../a/b.csv"));
		Assert.equals("../../../a/b.csv", FileUtil.normalize("../../../a/b.csv"));
	}

	@Test
	public void doubleNormalizeTest() {
		String normalize = FileUtil.normalize("/aa/b:/c");
		String normalize2 = FileUtil.normalize(normalize);
		Assert.equals("/aa/b:/c", normalize);
		Assert.equals(normalize, normalize2);
	}

	@Test
	public void subPathTest() {
		Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

		Path subPath = FileUtil.subPath(path, 5, 4);
		Assert.equals("eee", subPath.toString());
		subPath = FileUtil.subPath(path, 0, 1);
		Assert.equals("aaa", subPath.toString());
		subPath = FileUtil.subPath(path, 1, 0);
		Assert.equals("aaa", subPath.toString());

		// 负数
		subPath = FileUtil.subPath(path, -1, 0);
		Assert.equals("aaa/bbb/ccc/ddd/eee", subPath.toString().replace('\\', '/'));
		subPath = FileUtil.subPath(path, -1, Integer.MAX_VALUE);
		Assert.equals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -1, path.getNameCount());
		Assert.equals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -2, -3);
		Assert.equals("ddd", subPath.toString());
	}

	@Test
	public void subPathTest2() {
		String subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc/");
		Assert.equals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/");
		Assert.equals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/test.txt");
		Assert.equals("ccc/test.txt", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc");
		Assert.equals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc");
		Assert.equals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb");
		Assert.equals("", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb");
		Assert.equals("", subPath);
	}

	@Test
	public void getPathEle() {
		Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

		Path ele = FileUtil.getPathEle(path, -1);
		Assert.equals("fff", ele.toString());
		ele = FileUtil.getPathEle(path, 0);
		Assert.equals("aaa", ele.toString());
		ele = FileUtil.getPathEle(path, -5);
		Assert.equals("bbb", ele.toString());
		ele = FileUtil.getPathEle(path, -6);
		Assert.equals("aaa", ele.toString());
	}

	@Test
	public void listFileNamesTest() {
		List<String> names = FileUtil.listFileNames("classpath:");
		Assert.isTrue(names.contains("hutool.jpg"));

		names = FileUtil.listFileNames("");
		Assert.isTrue(names.contains("hutool.jpg"));

		names = FileUtil.listFileNames(".");
		Assert.isTrue(names.contains("hutool.jpg"));
	}

	@Test
	@Ignore
	public void listFileNamesInJarTest() {
		List<String> names = FileUtil.listFileNames("d:/test/hutool-core-5.1.0.jar!/cn/hutool/core/util ");
		for (String name : names) {
			Console.log(name);
		}
	}

	@Test
	@Ignore
	public void listFileNamesTest2() {
		List<String> names = FileUtil.listFileNames("D:\\m2_repo\\commons-cli\\commons-cli\\1.0\\commons-cli-1.0.jar!org/apache/commons/cli/");
		for (String string : names) {
			Console.log(string);
		}
	}

	@Test
	@Ignore
	public void loopFilesTest() {
		List<File> files = FileUtil.loopFiles("d:/");
		for (File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	@Ignore
	public void loopFilesTest2() {
		FileUtil.loopFiles("").forEach(Console::log);
	}

	@Test
	@Ignore
	public void loopFilesWithDepthTest() {
		List<File> files = FileUtil.loopFiles(FileUtil.file("d:/m2_repo"), 2, null);
		for (File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	public void getParentTest() {
		// 只在Windows下测试
		if (FileUtil.isWindows()) {
			File parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 0);
			Assert.equals(FileUtil.file("d:\\aaa\\bbb\\cc\\ddd"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 1);
			Assert.equals(FileUtil.file("d:\\aaa\\bbb\\cc"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 2);
			Assert.equals(FileUtil.file("d:\\aaa\\bbb"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 4);
			Assert.equals(FileUtil.file("d:\\"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 5);
			Assert.isNull(parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 10);
			Assert.isNull(parent);
		}
	}

	@Test
	public void lastIndexOfSeparatorTest() {
		String dir = "d:\\aaa\\bbb\\cc\\ddd";
		int index = FileUtil.lastIndexOfSeparator(dir);
		Assert.equals(13, index);

		String file = "ddd.jpg";
		int index2 = FileUtil.lastIndexOfSeparator(file);
		Assert.equals(-1, index2);
	}

	@Test
	public void getNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String name = FileUtil.getName(path);
		Assert.equals("ddd", name);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		name = FileUtil.getName(path);
		Assert.equals("ddd.jpg", name);
	}

	@Test
	public void mainNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String mainName = FileUtil.mainName(path);
		Assert.equals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd";
		mainName = FileUtil.mainName(path);
		Assert.equals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		mainName = FileUtil.mainName(path);
		Assert.equals("ddd", mainName);
	}

	@Test
	public void extNameTest() {
		String path =  FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "~/Desktop/hutool/ddd/";
		String mainName = FileUtil.extName(path);
		Assert.equals("", mainName);

		path =  FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd" : "~/Desktop/hutool/ddd";
		mainName = FileUtil.extName(path);
		Assert.equals("", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "~/Desktop/hutool/ddd.jpg";
		mainName = FileUtil.extName(path);
		Assert.equals("jpg", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.xlsx" : "~/Desktop/hutool/fff.xlsx";
		mainName = FileUtil.extName(path);
		Assert.equals("xlsx", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.gz" : "~/Desktop/hutool/fff.tar.gz";
		mainName = FileUtil.extName(path);
		Assert.equals("tar.gz", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.Z" : "~/Desktop/hutool/fff.tar.Z";
		mainName = FileUtil.extName(path);
		Assert.equals("tar.Z", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.bz2" : "~/Desktop/hutool/fff.tar.bz2";
		mainName = FileUtil.extName(path);
		Assert.equals("tar.bz2", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.xz" : "~/Desktop/hutool/fff.tar.xz";
		mainName = FileUtil.extName(path);
		Assert.equals("tar.xz", mainName);
	}

	@Test
	public void getWebRootTest() {
		File webRoot = FileUtil.getWebRoot();
		Assert.notNull(webRoot);
		Assert.equals("hutool-core", webRoot.getName());
	}

	@Test
	public void getMimeTypeTest() {
		String mimeType = FileUtil.getMimeType("test2Write.jpg");
		Assert.equals("image/jpeg", mimeType);

		mimeType = FileUtil.getMimeType("test2Write.html");
		Assert.equals("text/html", mimeType);

		mimeType = FileUtil.getMimeType("main.css");
		Assert.equals("text/css", mimeType);

		mimeType = FileUtil.getMimeType("test.js");
		Assert.equals("application/x-javascript", mimeType);

		// office03
		mimeType = FileUtil.getMimeType("test.doc");
		Assert.equals("application/msword", mimeType);
		mimeType = FileUtil.getMimeType("test.xls");
		Assert.equals("application/vnd.ms-excel", mimeType);
		mimeType = FileUtil.getMimeType("test.ppt");
		Assert.equals("application/vnd.ms-powerpoint", mimeType);

		// office07+
		mimeType = FileUtil.getMimeType("test.docx");
		Assert.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", mimeType);
		mimeType = FileUtil.getMimeType("test.xlsx");
		Assert.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", mimeType);
		mimeType = FileUtil.getMimeType("test.pptx");
		Assert.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation", mimeType);

		// pr#2617@Github
		mimeType = FileUtil.getMimeType("test.wgt");
		Assert.equals("application/widget", mimeType);
	}

	@Test
	public void isSubTest() {
		File file = new File("d:/test");
		File file2 = new File("d:/test2/aaa");
		Assert.isFalse(FileUtil.isSub(file, file2));
	}

	@Test
	public void isSubRelativeTest() {
		File file = new File("..");
		File file2 = new File(".");
		Assert.isTrue(FileUtil.isSub(file, file2));
	}

	@Test
	@Ignore
	public void appendLinesTest(){
		List<String> list = ListUtil.toList("a", "b", "c");
		FileUtil.appendLines(list, FileUtil.file("d:/test/appendLines.txt"), CharsetUtil.CHARSET_UTF_8);
	}

	@Test
	@Ignore
	public void createTempFileTest(){
		File nullDirTempFile = FileUtil.createTempFile();
		Assert.isTrue(nullDirTempFile.exists());

		File suffixDirTempFile = FileUtil.createTempFile(".xlsx",true);
		Assert.equals("xlsx", FileUtil.getSuffix(suffixDirTempFile));

		File prefixDirTempFile = FileUtil.createTempFile("prefix",".xlsx",true);
		Assert.isTrue(FileUtil.getPrefix(prefixDirTempFile).startsWith("prefix"));
	}

	@Test
	@Ignore
	public void getTotalLinesTest() {
		// 千万行秒级内返回
		final int totalLines = FileUtil.getTotalLines(FileUtil.file(""));
		Assert.equals(10000000, totalLines);
	}

	@Test
	public void isAbsolutePathTest(){
		String path = "d:/test\\aaa.txt";
		Assert.isTrue(FileUtil.isAbsolutePath(path));

		path = "test\\aaa.txt";
		Assert.isFalse(FileUtil.isAbsolutePath(path));
	}
}
