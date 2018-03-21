package converters;

import model.Category;
import model.CategoryFx;

	/**
	 * Developed by anisz
	 */

public class CategoryConverter {

	public static CategoryFx convertToCategoryFx(Category category) {
		CategoryFx categoryFx = new CategoryFx();
		categoryFx.setId(category.getId());
		categoryFx.setName(category.getName());
		return categoryFx;
	}
}
