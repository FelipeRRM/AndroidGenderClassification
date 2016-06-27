package feliperrm.trabalhoic.Util;


import com.google.gson.Gson;
import com.google.gson.internal.Streams;
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
    private Boolean needsToTrainNetwork;
    private static final String NEEDS_TRAIN_KEY = "needstrainingkey";

    public Boolean getNeedsToTrainNetwork() {
        if(needsToTrainNetwork==null)
            needsToTrainNetwork = Boolean.valueOf(Geral.loadSharedPreference(MyApp.getContext(), NEEDS_TRAIN_KEY, String.valueOf(true)));
        return needsToTrainNetwork;
    }

    public void setNeedsToTrainNetwork(Boolean needsToTrainNetwork) {
        Geral.salvarSharedPreference(MyApp.getContext(), NEEDS_TRAIN_KEY, String.valueOf(needsToTrainNetwork));
        this.needsToTrainNetwork = needsToTrainNetwork;
    }

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

    public void addToCategories(Category category){
        ArrayList<Category> categories = getCategories();
        categories.add(category);
        setCategories(categories);
    }

    public boolean isStringInCategory(String title){
        for (Category category : getCategories())
            if(category.getName().equals(title))
                return true;
        return false;
    }

    public boolean isFileInCategory(String fileName, String categoryName){
        for (Category category : getCategories()) {
            if (category.getName().equals(categoryName)) {
                for (String tempFileName: category.getFiles())
                    if(fileName.equals(tempFileName))
                        return true;
            }
        }
        return false;
    }

    public void addFileToCategory(String fileName, String categoryName){
        ArrayList<Category> categories = getCategories();
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                category.getFiles().add(fileName);
            }
        }
        setCategories(categories);
    }
}
