/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

//Implementation by Daniel Baktiar
//http://dbaktiar.wordpress.com/2009/06/26/float-and-byte-array-conversion/

public class ByteFloatConvert {

    private static final int MASK = 0xff;

    /**
     * convert byte array (of size 4) to float
     * @param test
     * @return
     */
    public static float byteArrayToFloat(byte test[]) {
            int bits = 0;
            int i = 0;
            for (int shifter = 3; shifter >= 0; shifter--) {
                    bits |= ((int) test[i] & MASK) << (shifter * 8);
                    i++;
            }

            return Float.intBitsToFloat(bits);
    }

    /**
     * convert float to byte array (of size 4)
     * @param f
     * @return
     */
    public static byte[] floatToByteArray(float f) {
            int i = Float.floatToRawIntBits(f);
            return intToByteArray(i);
    }

    /**
     * convert int to byte array (of size 4)
     * @param param
     * @return
     */
    public static byte[] intToByteArray(int param) {
            byte[] result = new byte[4];
            for (int i = 0; i < 4; i++) {
                    int offset = (result.length - 1 - i) * 8;
                    result[i] = (byte) ((param >>> offset) & MASK);
            }
            return result;
    }

    /**
     * convert byte array to String.
     * @param byteArray
     * @return
     */
    public static String byteArrayToString(byte[] byteArray) {
            StringBuilder sb = new StringBuilder("[");
            if(byteArray == null) {
                    throw new IllegalArgumentException("byteArray must not be null");
            }
            int arrayLen = byteArray.length;
            for(int i = 0; i < arrayLen; i++) {
                    sb.append(byteArray[i]);
                    if(i == arrayLen - 1) {
                            sb.append("]");
                    } else{
                            sb.append(", ");
                    }
            }
            return sb.toString();
    }
    
}
