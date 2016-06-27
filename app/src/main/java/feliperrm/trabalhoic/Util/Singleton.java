package feliperrm.trabalhoic.Util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import feliperrm.trabalhoic.Model.Category;

/**
 * Created by felip on 17/05/2016.
 */
public class Singleton {

    private static Singleton singleton;
    ArrayList<Category> categories;
    private static final String CATEGORIES_KEY = "categorieskey";

    public ArrayList<Category> getCategories() {
        if(categories==null) {
            Gson gson = new Gson();
            String emptyArray = gson.toJson(new ArrayList<Category>());
            categories = gson.fromJson(Geral.loadSharedPreference(MyApp.getContext(), CATEGORIES_KEY, emptyArray), new TypeToken<ArrayList<Category>>(){}.getType());
        }
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        Geral.salvarSharedPreference(MyApp.getContext(), CATEGORIES_KEY, new Gson().toJson(categories));
        this.categories = categories;
    }

    public static Singleton getSingleton(){
        if(singleton==null)
            singleton = new Singleton();
        return singleton;
    }

    public void removeCategoria(int position) {
        ArrayList<Category> categories = getCategories();
        categories.remove(position);
        setCategories(categories);
    }
}
