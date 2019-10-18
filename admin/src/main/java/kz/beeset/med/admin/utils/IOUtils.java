package kz.beeset.med.admin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class IOUtils {

    /**
     * Размер буфера по умолчанию для использования
     * {@link #copyLarge(InputStream, OutputStream)}
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    // read toByteArray
    //-----------------------------------------------------------------------
    /**
     * Получите содержимое <code>InputStream</code> как <code>byte[]</code>.
     * <p>
     * Этот метод буферизирует вход внутри, поэтому нет необходимости использовать
     * <code>BufferedInputStream</code>.
     *
     * @param input <code>InputStream</code> читать из
     * @return запрошенный массив байтов
     * @throws NullPointerException если ввод равен нулю
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    // copy from InputStream
    //-----------------------------------------------------------------------
    /**
     * Копирование байтов из <code>InputStream</code> для
     * <code>OutputStream</code>.
     * <p>
     * Этот метод буферизирует вход внутри, поэтому нет необходимости использовать
     * <code>BufferedInputStream</code>.
     * <p>
     * Большие потоки (более 2 ГБ) вернут значение, скопированное байтами
     * <code>-1</code> после завершения копирования, так как правильное
     * количество байтов не может быть возвращено как int.
     * Для больших потоков используйте метод <code>copyLarge(InputStream, OutputStream)</code>.
     *
     * @param input <code>InputStream</code> читать из
     * @param output <code>OutputStream</code> написать
     * @return количество копируемых байтов или -1 если &gt; Integer.MAX_VALUE
     * @throws NullPointerException если вход или выход равен нулю
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * Копирование байтов из <code>InputStream</code> для
     * <code>OutputStream</code>.
     * <p>
     * Этот метод буферизирует вход внутри, поэтому нет необходимости использовать
     * <code>BufferedInputStream</code>.
     *
     * @param input <code>InputStream</code> читать из
     * @param output <code>OutputStream</code> написать
     * @return количество копируемых байтов
     * @throws NullPointerException если вход или выход равен нулю
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
