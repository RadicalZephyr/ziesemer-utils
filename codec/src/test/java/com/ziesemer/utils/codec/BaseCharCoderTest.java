package com.ziesemer.utils.codec;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;

import com.ziesemer.utils.codec.impl.Base64Decoder;
import com.ziesemer.utils.codec.impl.Base64Encoder;
import com.ziesemer.utils.codec.impl.HexDecoder;
import com.ziesemer.utils.codec.impl.HexEncoder;
import com.ziesemer.utils.codec.impl.URLDecoder;
import com.ziesemer.utils.codec.impl.URLEncoder;
import com.ziesemer.utils.codec.test.SingleByteDecoder;
import com.ziesemer.utils.codec.test.SingleByteEncoder;
import com.ziesemer.utils.junit.BaseTest;
import com.ziesemer.utils.junit.DescParameterizedTestSuite;
import com.ziesemer.utils.junit.DescParameterizedTestSuite.ParameterDescription;
import com.ziesemer.utils.junit.DescParameterizedTestSuite.Parameters;

/**
 * @author Mark A. Ziesemer
 * 	<a href="http://www.ziesemer.com.">&lt;www.ziesemer.com&gt;</a>
 */
@RunWith(DescParameterizedTestSuite.class)
public abstract class BaseCharCoderTest extends BaseTest{
	
	protected final TestFactory testFactory;
	
	protected final byte[] rawTest = buildRawTest();
	protected final String encodedTest;
	
	protected BaseCharCoderTest(TestFactory testFactory){
		this.testFactory = testFactory;
		this.encodedTest = testFactory.getEncodedTest();
	}
	
	public static byte[] buildRawTest(){
		return new byte[]{0x00, 0x01,
			0x20, 0x21, 0x2A, 0x2B, 0x2D, 0x2E, 0x2F,
			0x30, 0x39, 0x3D, 0x41, 0x5A, 0x5F, 0x61, 0x7A,
			// Including 0xFF (-1) is a good test for testing against premature end-of-stream.
			(byte)0xFF};
	}
	
	@Parameters
	public static Collection<Object[]> getParameters(){
		return Arrays.asList(new Object[][]{
			{new TestFactory("SingleByte"){
				@Override
				public IByteToCharEncoder newEncoder(){
					return new SingleByteEncoder();
				}
				@Override
				public ICharToByteDecoder newDecoder(){
					return new SingleByteDecoder();
				}
				@Override
				public String getEncodedTest(){
					return "\u0000\u0001 !*+-./09=AZ_az\uFFFF";
				}
				@Override
				public String getShortEncodedTest(){
					return "\uFFFF";
				}
			}},
			{new TestFactory("Base64"){
				@Override
				public IByteToCharEncoder newEncoder(){
					return new Base64Encoder();
				}
				@Override
				public ICharToByteDecoder newDecoder(){
					return new Base64Decoder();
				}
				@Override
				public String getEncodedTest(){
					return "AAEgISorLS4vMDk9QVpfYXr/";
				}
				@Override
				public String getShortEncodedTest(){
					return "/w==";
				}
			}},
			{new TestFactory("Hex"){
				@Override
				public IByteToCharEncoder newEncoder(){
					return new HexEncoder();
				}
				@Override
				public ICharToByteDecoder newDecoder(){
					return new HexDecoder();
				}
				@Override
				public String getEncodedTest(){
					return "000120212A2B2D2E2F30393D415A5F617AFF";
				}
				@Override
				public String getShortEncodedTest(){
					return "FF";
				}
			}},
			{new TestFactory("URL"){
				@Override
				public IByteToCharEncoder newEncoder(){
					return new URLEncoder();
				}
				@Override
				public ICharToByteDecoder newDecoder(){
					return new URLDecoder();
				}
				@Override
				public String getEncodedTest(){
					return "$00$01+$21*$2B-.$2F09$3DAZ_az$FF";
				}
				@Override
				public String getShortEncodedTest(){
					return "$FF";
				}
			}}
		});
	}
	
	@ParameterDescription
	public static Object getDescription(TestFactory tf){
		return tf.getDescription();
	}
	
	protected abstract static class TestFactory{
		protected String description;
		
		protected TestFactory(){
			super();
		}
		
		public TestFactory(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
		
		public abstract IByteToCharEncoder newEncoder();
		public abstract ICharToByteDecoder newDecoder();
		public abstract String getEncodedTest();
		public String getEmptyEncodedTest(){
			return "";
		}
		public abstract String getShortEncodedTest();
		public byte[] getShortDecodedTest(){
			return new byte[]{(byte)0xFF};
		}
	}
	
}
