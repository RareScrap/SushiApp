package com.webtrust.sushiapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Адаптер, связывающий данные элемента главного меню с TableLayout ListView
 * @author RareScrap
 */

public class MenusAdapter extends ArrayAdapter<MenuItem> {
    // ViewHolder, для элементов меню
    private static class ViewHolder {
        ImageView menuPic;
        TextView menuName;
    }

    // Кэш для уже загруженных объектов Bitmap
    private Map<String, Bitmap> bitmaps = new HashMap<>();

    /**
     * Конструктор для инициализации унаследованных членов суперкласса
     * @param context Активность в которой отображается ListView
     * @param menuItem Список выводимых данных; Список элементов меню
     */
    public MenusAdapter(Context context, List<MenuItem> menuItem) {
        /*
        Второй аргумент конструктора суперкласса представляет идентификатор ресурса
        макета, содержащего компонент TextView, в котором отображаются данные ListView.
        Аргумент –1 означает, что в приложении используется пользовательский макет,
        чтобы элемент списка не ограничивался одним компонентом TextView.
         */
        super(context, -1, menuItem);
    }

    /** Создание пользовательских представлений для элементов ListView.
     * Переопределение этого метода позволяет связать данные с нестандартным элементом ListView.
     *
     * @param position Позиция элемента в списке (массиве) ListView
     * @param menuElementView Элемент View, представляющий элемент меню
     * @param parent Родитель (контейнер) элемента списка
     * @return {@link View} элемента списка для отображения на макете
     */
    @Override
    public View getView(int position, View menuElementView, ViewGroup parent) {
        // Получение объекта MenuItem для заданной позиции ListView
        // menuItem - это объект элемента меню (только поля), а menuElementView - его View
        MenuItem menuItem = getItem(position); // TODO: Как это работет?

        //Объект, содержащий ссылки на представления элемента списка
        ViewHolder viewHolder;

        // Проверить возможность повторного использования ViewHolder для элемента, вышедшего за границы экрана
        if (menuElementView == null) { // Объекта ViewHolder нет, создать его
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext()); // Инфлатер для заполнения
            /* Заполнение макета элемента меню
            Parent - родительский объект ViewGroup макета, к которому будут присоединены его представления.
            В последнем аргументе передается флаг автоматического присоединения представлений.
            В данном случае третий аргумент равен false, потому что ListView вызывает метод getView
            для получения представления View элемента списка, а затем присоединяет его к ListView*/
            menuElementView = inflater.inflate(R.layout.fragment_main_menu_list_item, parent, false);

            // Получение ссылок на на представления в заполняемом макете
            viewHolder.menuPic = (ImageView) menuElementView.findViewById(R.id.menuPic);
            viewHolder.menuName = (TextView) menuElementView.findViewById(R.id.menuName);
            menuElementView.setTag(viewHolder); // Связывает новый объект ViewHolder с элементом ListView для использования в будущем.
        }else { // Cуществующий объект ViewHolder используется заново
            viewHolder = (ViewHolder) menuElementView.getTag();
        }

        /* Если картинка меню уже загружена, использовать его;
        в противном случае загрузить в отдельном потоке */
        if (bitmaps.containsKey(menuItem.picURL)) {
            viewHolder.menuPic.setImageBitmap(
                    bitmaps.get(menuItem.picURL));
        }else {
            // Загрузить и вывести значок погодных условий
            new LoadImageTask(viewHolder.menuPic).execute(menuItem.picURL);
        }

        // Получить данные из объекта MenuItem и заполнить представления
        Context context = getContext(); // Для загрузки строковых ресурсов. Нужно для context.getString в setText() ниже

        // Назначается текст компонентов TextView элемента ListView
        viewHolder.menuName.setText("ТЕКСТ_ИЗ_JSON");

        return menuElementView; // Вернуть готовое представление элемента
    }

    // TODO: Как полученное изображение присваивается viewHolder и представлению?
    // Кажись, изменение imageView так же изменяет и аргумент, переданный в конструкторе LoadImageTask(). Таким образом, создается нечно вроде "ссылки"
    // AsyncTask для загрузки изображения в отдельном потоке
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView; // Для вывода картинки

        // Сохранение ImageView для загруженного объекта Bitmap
        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        // загрузить изображение; params[0] содержит URL-адрес изображения
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]); // Создать URL для изображения

                // Открыть объект HttpURLConnection, получить InputStream и загрузить изображение
                connection = (HttpURLConnection) url.openConnection(); // Преобразование типа необходимо, потому что метод возвращает URLConnection

                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap); // Кэширование
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally { // Этот участок кода будет выполняться независимо от того, какие исключения были возбуждены и перехвачены
                connection.disconnect(); // Закрыть HttpURLConnection
            }

            return bitmap;
        }

        // Связать картинку с элементом списка
        // Выполняется в потоке GUI вроде как для вывода изображения
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
