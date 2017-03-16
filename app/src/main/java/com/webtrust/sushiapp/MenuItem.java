package com.webtrust.sushiapp;

import android.widget.ImageView;

/**
 * Класс @link MenuItem описывает элемент главного меню приложения (категория блюда)
 */
public class MenuItem {
    public final String menuName; // Название меню
    public final String picURL; // Ссылка на картинку меню
    public final ImageView menuPic; // Картинка меню

    public MenuItem(String menuName, String picURL, ImageView menuPic) {
        this.menuName = menuName;
        this.picURL = picURL;
        this.menuPic = menuPic;
    }
}
