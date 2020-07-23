
package com.estarimage;


public class EstarImage {
   
    
	public native boolean TestUsed();

	public native String  GetStringFromJNI();
    
	// 更新JPEG图像的创建者信息。sCreaterID为创建者ID
	// TRUE   更新成功
	// FALSE  更新失败
	// JPEG文件基本信息
	public native boolean UpdateJPegCreaterInfo(String szInputFile,String sCreaterID);

	// 加密JPEG图像文件
	// 1  加密成功,加密成功后文件名后缀改为.est
	// 0  加密失败
	// -1 不支持的文件类型
	public native int EncryptJPegFile(String szInputFile);

	// 解密JPEG图像文件
	// 1  解密成功,解密前文件名后缀为.est
	// 0  解密失败
	// -1 不支持的文件类型
	public native int DecryptJPegFile(String szInputFile);

	// 按照比例压缩szInputFile文件;fRate为压缩比例,取值为0～1;nQuality为图像品质,取值为1～100
	// nLimitSize!=0 则表示限制大小，为0不考虑长度
	// nQuality  压缩成功, 生成文件szOutputFile, 但压缩方法使用的是按照CompressPictureBySize(nLimitSize,nQuality)进行的。
	// 2  压缩成功, 生成文件szOutputFile
	// 1  因为大小已经小于设定的大小，没有做压缩处理，但生成文件szOutputFile
	// 0  压缩失败, 没有生成szOutputFile
	// -1 不支持的文件类型
	// 目前输入文件支持BMP/JPG/JPEG,输出文件支持BMP/JPG/JPEG。输出文件为BMP时输入文件只能是BMP文件。
	public native int CompressPictureByRate(String szInputFile,String szOutputFile, float fRate,int nQuality, int nLimitSize);

	// 压缩Picture文件到设定的大小
	// 如果文件大小大于nKSize, 则压缩到nKSize大小。nQuality为图像品质1～100。
	// 2  压缩成功, 生成文件szOutputFile
	// 1  因为大小已经小于设定的大小，没有做压缩处理，但生成文件szOutputFile
	// 0  压缩失败, 没有生成szOutputFile
	// -1 不支持的文件类型
	public native int CompressPictureBySize(String szInputFile,String szOutputFile, int nKSize,int nQuality);

	// 压缩szInputFile文件到设定分辨率;nWidth*nHeight为设定的分辨率;nQuality为图像品质,取值为1～100
	// 如果文件分辨率大于nWidth*nHeight, 则压缩到nWidth*nHeight。
	// nLimitSize!=0 则表示限制大小，为0不考虑长度
	/* 返回值如下：
		nQuality+100 压缩成功, 生成文件szOutputFile, 压缩方法不变，但质量下降为nQuality
		nQuality     压缩成功, 生成文件szOutputFile, 但压缩方法使用的是按照CompressPictureBySize(nLimitSize,nQuality)进行的。
  			2         压缩成功, 生成文件szOutputFile
  			1         因为分辨率低于设定的分辨率，没有做压缩处理，但生成文件szOutputFile
  			0         压缩失败, 没有生成szOutputFile
 		   -1         不支持的文件类型
	*/
	// 目前输入文件支持BMP/JPG/JPEG,输出文件支持BMP/JPG/JPEG。输出文件为BMP时输入文件只能是BMP文件。
	public native int CompressPictureByResolution(String szInputFile,String szOutputFile, int nWidth, int nHeight,int nQuality, int nLimitSize);

	// 压缩szInputFile文件到设定分辨率;nWidth*nHeight为设定的分辨率;nQuality为图像品质,取值为1～100
	// 如果文件分辨率大于nWidth*nHeight, 则压缩到nWidth*nHeight, 这时不管文件的大小是否已经在限定范围内都做。
	// nLimitSize!=0 则表示限制大小，为0不考虑长度
	/* 返回值如下：
		nQuality+100 压缩成功, 生成文件szOutputFile, 压缩方法不变，但质量下降为nQuality
		nQuality     压缩成功, 生成文件szOutputFile, 但压缩方法使用的是按照CompressPictureBySize(nLimitSize,nQuality)进行的。
  			2         压缩成功, 生成文件szOutputFile
  			1         因为分辨率低于设定的分辨率，没有做压缩处理，但生成文件szOutputFile
  			0         压缩失败, 没有生成szOutputFile
 		   -1         不支持的文件类型
	*/
	// 目前输入文件支持BMP/JPG/JPEG,输出文件支持BMP/JPG/JPEG。输出文件为BMP时输入文件只能是BMP文件。
	public native int CompressPictureByResolution2(String szInputFile,String szOutputFile, int nWidth, int nHeight,int nQuality, int nLimitSize);

	// 更新JPEG图像文件的GPS信息。如果szOutputFile为_T("")或者与szInputFile同名，则直接更新输入文件，否则更新结果到输出文件
	// dLatitude;		// 纬度
	// dLongitude;		// 经度
	// 1  更新信息成功
	// 0  没有更新
	// -1 更新失败
	public native int UpdateJpegGPSInfo(String szInputFile,String szOutputFile, double dLatitude,double dLongitude);

	// 测试JPEG图像是否被损坏
	// TRUE   没有被损坏
	// FALSE  已被损坏
	public native boolean CheckJPegFile(String szInputFile);

	// 旋转szInputFile;nDegree为旋转角度，取值为90/180/270三种
	// szTmpFile1,szTmpFile1 是两个中间文件名，用于旋转用，是合法可读写的文件即可
	// 1  旋转成功，生成文件szOutputFile
	// 0  失败
	public native int RotateJpegFile(String szInputFile, String szOutputFile, int nDegree,String szTmpFile1,String sTmpFile2);

	static {
	   System.loadLibrary("estar-image");
	}
}
