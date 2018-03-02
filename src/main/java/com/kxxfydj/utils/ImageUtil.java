package com.kxxfydj.utils;

import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by kxxfydj on 2018/3/2.
 */
public class ImageUtil {
	private static final Logger log = getLogger(ImageUtil.class);

	private ImageUtil() {
	}

	public static boolean generateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) { // 图像数据为空
			return false;
		}
		try {
			// Base64解码
			byte[] bytes = Base64.decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			return generateImage(bytes, imgFilePath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean generateImage(byte[] imgBytes, String imgFilePath) {
		String folderPath = imgFilePath.substring(0,imgFilePath.lastIndexOf(File.separator));
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		// 生成jpeg图片
		try (OutputStream out = new FileOutputStream(imgFilePath)) {
			out.write(imgBytes);
			out.flush();
			out.close();
			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 对图片进行指定行数和列数进行切割
	 * 
	 * @param imageByte
	 *            图片流
	 * @param targetDir
	 *            切割后的保存目录
	 * @param rows
	 *            切割行
	 * @param cols
	 *            切割列
	 * @return
	 */
	public static List<BufferedImage> cutImageByRowColumn(byte[] imageByte, String targetDir, int rows, int cols) {
		List<BufferedImage> list = null;
		try {
			BufferedImage source = ImageIO.read(new ByteArrayInputStream(imageByte));
			if (source != null) {
				list = new ArrayList<>();
				int sWidth = source.getWidth(); // 图片宽度
				int sHeight = source.getHeight(); // 图片高度

				int eWidth = sWidth / cols; // 切割图片宽度
				int eHeight = sHeight / rows; // 切割图片高度
				BufferedImage image;
				String fileName;
				File file = new File(targetDir);
				if (!file.exists()) { // 存储目录不存在，则创建目录
					file.mkdirs();
				}
				int index = 0;
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// x坐标,y坐标,宽度,高度
						image = source.getSubimage(j * eWidth, i * eHeight, eWidth, eHeight);
						fileName = targetDir + "/" + index++ + ".png";
						file = new File(fileName);
						ImageIO.write(image, "png", file);
						list.add(image);
					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 对图片进行指定的宽高切割,剩余部分被丢弃
	 * 
	 * @param imageByte
	 * @param targetDir
	 * @param width
	 * @param height
	 * @return
	 */
	public static List<BufferedImage> cutImageBySize(byte[] imageByte, String targetDir, int width, int height) {
		List<BufferedImage> list = null;
		try {
			BufferedImage source = ImageIO.read(new ByteArrayInputStream(imageByte));
			if (source != null) {
				list = new ArrayList<>();
				int sWidth = source.getWidth(); // 图片宽度
				int sHeight = source.getHeight(); // 图片高度
				if (sWidth >= width && sHeight >= height) {
					int cols = sWidth / width;
					int rows = sHeight / height;
					String fileName;
					File file = new File(targetDir);
					if (!file.exists()) { // 存储目录不存在，则创建目录
						file.mkdirs();
					}
					BufferedImage image;
					int index = 0;
					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < cols; j++) {
							// x坐标,y坐标,宽度,高度
							image = source.getSubimage(j * width, i * height, width, height);
							fileName = targetDir + "/" + index++ + ".png";
							file = new File(fileName);
							ImageIO.write(image, "png", file);
							list.add(image);
						}
					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * 切割图片
	 * 
	 * @param imageByte
	 *            图片
	 * @param targetDir
	 *            目录
	 * @param fileName
	 *            文件名称
	 * @param x
	 *            开始x坐标
	 * @param y
	 *            开始y坐标
	 * @param offsetWidth
	 *            裁掉的宽度
	 * @param offsetHeight
	 *            裁掉的高度
	 * @return
	 */
	public static BufferedImage getSubImage(byte[] imageByte, String targetDir, String fileName, int x, int y,
			int offsetWidth, int offsetHeight) {
		BufferedImage image = null;
		try {
			BufferedImage source = ImageIO.read(new ByteArrayInputStream(imageByte));
			if (source != null) {
				int sWidth = source.getWidth();
				int sHeight = source.getHeight();
				image = source.getSubimage(x, y, sWidth + offsetWidth, sHeight - offsetHeight);
				File file = new File(targetDir);
				if (!file.exists()) {
					file.mkdirs();
				}
				ImageIO.write(image, "png", new File(targetDir + File.separator + fileName));
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return image;
	}

	/**
	 * 切割图片
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage getSubImage(BufferedImage image, int x, int y, int width, int height) {
		return image.getSubimage(x, y, width, height);
	}

	/**
	 * file转byte
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] File2byte(File file) {
		byte[] buffer = null;
		try(FileInputStream fis = new FileInputStream(file)) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} 
		return buffer;
	}

	public static void main(String[] args) throws IOException {
		// String imgStr =
		// "data:image/gif;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAQACgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDxvSvDR1BLd7vVtN0lLp9ts2oNIom+baWGxG2oG43vtXIYAna22Twd4O1Txxrn9k6T5CzLE00kk77UjQYGTgEnkqOAevpkj0C9i1m5hWS90aAeHbXwpHi/u7JCpc2CiPy55QSG890ASJh82TtyXJr+DdMs9Q8PNNpkPiN4bTT7hL5LTRhKl1dTI0GVcTfM0aXKkIQPlR2G0u2QDz/TNBF/p0t/darY6ZapKIUe8Ex858EkII43J2jbu9N6Z+8KuXvg250nxLqeh6xqWm6ZPpyB5JbiR2SQEpt2bEZmJDq2NuQMk4wauaP4Lv7qzfXINE1XVdM8147GKKzkzdlT1k8vPlxjjdhsk5RDwzxxzeG/E2raze6xHpcniJP7TmS4uLCJpoLiVWDP/qsEI28EEbchvlxg4AMvxF4bvPDV5awXUkE0d3aRXttPAxKTQyDKsAwDDoRhgDx0xgkq549ha38Z3sUpjE6pD58UUiulvL5SeZAm0kBI23RqoJ2hAuTiigD/2Q==";
		// String imgFile = "d:/temp/a111.jpg";
		// ImageUtil.generateImage(imgStr.replace("data:image/gif;base64,",""),imgFile);
		File file = new File("E:\\image\\zhmxcx (2).png");
		ImageUtil.getSubImage(File2byte(file), "E:\\image\\123", "test.png", 0, 25, 0, 50);
	}
}
