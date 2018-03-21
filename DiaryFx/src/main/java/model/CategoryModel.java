package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import utils.ApplicationException;
import utils.Queries;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Diary.Main;
import converters.CategoryConverter;

/**
 * Developed by anisz
 */

public class CategoryModel {

    private ObservableList<CategoryFx> categoryList = FXCollections.observableArrayList();
    private ObjectProperty<CategoryFx> category = new SimpleObjectProperty<>();
    private TreeItem<String> root = new TreeItem<>();


    public void init() throws ApplicationException {
        List<Category> categories = Queries.queryForAll(Category.class);
        initCategoryList(categories);
        initRoot(categories);
    }

    private void initRoot(List<Category> categories) { // tu też się dostaje zwykła lista
        this.root.getChildren().clear();
        categories.forEach(c->{
            TreeItem<String> categoryItem = new TreeItem<>(c.getName());
            c.getEvents().forEach(b->{ // tu się wywala dodawanie kategorii jak nie ma książek bo jest nullPointerException, pomogło odświeżenie
                categoryItem.getChildren().add(new TreeItem<>(b.getTitle()));
            });
            root.getChildren().add(categoryItem);
        });
    }

    private void initCategoryList(List<Category> categories) {
        this.categoryList.clear();
        categories.forEach(c -> {
            CategoryFx categoryFx = CategoryConverter.convertToCategoryFx(c);
            this.categoryList.add(categoryFx);
        });
    }

    public void deleteCategoryById() throws ApplicationException, SQLException {
        Queries.deleteById(Category.class, category.getValue().getId()); // //tu się posługuję tą Observable category!!
//        Queries.deleteByColumnName(Event.CATEGORY_ID, category.getValue().getId()); // Tutaj chyba kasowanie kaskadowe, hehe chyba działa
        init();
    }

    public void saveCategoryInDataBase(String name) throws ApplicationException {
        Category category = new Category(); // a tu tworzę zwykłą nową kategorię
        category.setName(name);
        Queries.create(category);
        init();
    }

    public void updateCategoryInDataBase() throws ApplicationException {
        Category tempCategory = Queries.findById(Category.class, getCategory().getId());
        tempCategory.setName(getCategory().getName()); // tutaj zwykłej kategorii ustawiam nazwę pobrana z observable kategorii
        Queries.create(tempCategory); //i zapisuję do bazy zwykłą kategorię
        init();
    }

    public ObjectProperty<CategoryFx> categoryProperty() {
        return category;
    }
    
    public ObservableList<CategoryFx> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ObservableList<CategoryFx> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryFx getCategory() {
        return category.get();
    }

    public void setCategory(CategoryFx category) {
        this.category.set(category);
    }

    public TreeItem<String> getRoot() {
        return root;
    }

    public void setRoot(TreeItem<String> root) {
        this.root = root;
    }
}