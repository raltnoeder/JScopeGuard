package scopeguard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class ScopeGuardOps
{
    /**
     * Closes an InputStream, instances of IOException are caught and ignored.
     *
     * @param iStream The InputStream to close
     * @return Runnable that performs the action when executed
     */
    public static Runnable closeIgnoreExc(final InputStream iStream)
    {
        return new InputStreamCloser(iStream);
    }

    /**
     * Closes an OutputStream, instances of IOException are caught and ignored.
     *
     * @param oStream The OutputStream to close
     * @return Runnable that performs the action when executed
     */
    public static Runnable closeIgnoreExc(final OutputStream oStream)
    {
        return new OutputStreamCloser(oStream);
    }

    /**
     * Closes a Reader, instances of IOException are caught and ignored.
     *
     * @param readerRef The Reader to close
     * @return Runnable that performs the action when executed
     */
    public static Runnable closeIgnoreExc(final Reader readerRef)
    {
        return new ReaderCloser(readerRef);
    }

    /**
     * Closes a Writer, instances of IOException are caught and ignored.
     *
     * @param writerRef The Writer to close
     * @return Runnable that performs the action when executed
     */
    public static Runnable closeIgnoreExc(final Writer writerRef)
    {
        return new WriterCloser(writerRef);
    }

    /**
     * Closes an AutoClosable, instances of Exception are caught and ignored.
     *
     * @param acRef The AutoClosable to close
     * @return Runnable that performs the action when executed
     */
    public static Runnable closeIgnoreExc(final AutoCloseable acRef)
    {
        return new Closer(acRef);
    }

    private static class Closer implements Runnable
    {
        private AutoCloseable acObj;

        Closer(AutoCloseable acRef)
        {
            acObj = acRef;
        }

        @Override
        public void run()
        {
            try
            {
                acObj.close();
            }
            catch (Exception ignored)
            {
            }
        }
    }

    private static class InputStreamCloser implements Runnable
    {
        private InputStream iStream;

        InputStreamCloser(InputStream iStreamRef)
        {
            iStream = iStreamRef;
        }

        @Override
        public void run()
        {
            try
            {
                iStream.close();
            }
            catch (IOException ignored)
            {
            }
        }
    }

    private static class OutputStreamCloser implements Runnable
    {
        private OutputStream oStream;

        OutputStreamCloser(OutputStream oStreamRef)
        {
            oStream = oStreamRef;
        }

        @Override
        public void run()
        {
            try
            {
                oStream.close();
            }
            catch (IOException ignored)
            {
            }
        }
    }

    private static class ReaderCloser implements Runnable
    {
        private Reader readerObj;

        ReaderCloser(Reader readerRef)
        {
            readerObj = readerRef;
        }

        @Override
        public void run()
        {
            try
            {
                readerObj.close();
            }
            catch (IOException ignored)
            {
            }
        }
    }

    private static class WriterCloser implements Runnable
    {
        private Writer writerObj;

        WriterCloser(Writer writerRef)
        {
            writerObj = writerRef;
        }

        @Override
        public void run()
        {
            try
            {
                writerObj.close();
            }
            catch (IOException ignored)
            {
            }
        }
    }
}
