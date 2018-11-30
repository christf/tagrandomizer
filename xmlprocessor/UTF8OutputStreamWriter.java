package xmlprocessor;


//Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.geocities.com/kpdus/jad.html
//Decompiler options: packimports(3) 
//Source File Name:   UTF8OutputStreamWriter.java


import com.sun.org.apache.xerces.internal.util.XMLChar;
import java.io.*;

public final class UTF8OutputStreamWriter extends Writer
{

 public UTF8OutputStreamWriter(OutputStream out)
 {
     lastUTF16CodePoint = 0;
     this.out = out;
 }

 public String getEncoding()
 {
     return "UTF-8";
 }

 public void write(int c)
     throws IOException
 {
     if(lastUTF16CodePoint != 0)
     {
         int uc = ((lastUTF16CodePoint & 0x3ff) << 10 | c & 0x3ff) + 0x10000;
         if(uc < 0 || uc >= 0x200000)
         {
             throw new IOException((new StringBuilder()).append("Atttempting to write invalid Unicode code point '").append(uc).append("'").toString());
         } else
         {
             out.write(0xf0 | uc >> 18);
             out.write(0x80 | uc >> 12 & 0x3f);
             out.write(0x80 | uc >> 6 & 0x3f);
             out.write(0x80 | uc & 0x3f);
             lastUTF16CodePoint = 0;
             return;
         }
     }
     if(c < 128)
         out.write(c);
     else
     if(c < 2048)
     {
         out.write(0xc0 | c >> 6);
         out.write(0x80 | c & 0x3f);
     } else
     if(c <= 65535)
         if(!XMLChar.isHighSurrogate(c) && !XMLChar.isLowSurrogate(c))
         {
             out.write(0xe0 | c >> 12);
             out.write(0x80 | c >> 6 & 0x3f);
             out.write(0x80 | c & 0x3f);
         } else
         {
             lastUTF16CodePoint = c;
         }
 }

 public void write(char cbuf[])
     throws IOException
 {
     for(int i = 0; i < cbuf.length; i++)
         write(cbuf[i]);

 }

 public void write(char cbuf[], int off, int len)
     throws IOException
 {
     for(int i = 0; i < len; i++)
         write(cbuf[off + i]);

 }

 public void write(String str)
     throws IOException
 {
     int len = str.length();
     for(int i = 0; i < len; i++)
         write(str.charAt(i));

 }

 public void write(String str, int off, int len)
     throws IOException
 {
     for(int i = 0; i < len; i++)
         write(str.charAt(off + i));

 }

 public void flush()
     throws IOException
 {
     out.flush();
 }

 public void close()
     throws IOException
 {
     if(lastUTF16CodePoint != 0)
     {
         throw new IllegalStateException("Attempting to close a UTF8OutputStreamWriter while awaiting for a UTF-16 code unit");
     } else
     {
         out.close();
         return;
     }
 }

 OutputStream out;
 int lastUTF16CodePoint;
}
