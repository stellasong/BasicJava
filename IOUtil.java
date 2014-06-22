public final class IOUtil {
  private IOUtil() {}

  public static void closeQuietly(Closeable... closeables) {
    for (Closeable c : closeables) {
        if (c != null) try {
          c.close();
        } catch(Exception ex) {}
    }
  }
}

//Then your code would be reduced to:
/*
try {
  copy(in, out);
} finally {
  IOUtil.closeQuietly(in, out);
}
*/
