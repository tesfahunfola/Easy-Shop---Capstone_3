function loadCategories(categories)
{
    const select = document.getElementById('category-select');
    console.log(categories);

    categories.forEach(c => {
        const option = document.createElement('option');
        option.setAttribute('value', c.categoryId);
        option.innerText = c.name;
        select.appendChild(option);
    })
}

document.addEventListener('DOMContentLoaded', () => {
})
