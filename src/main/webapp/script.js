const $tbody = $('#item-body');
const $addButton = $('#add-item-button');
const $descriptionInput = $('#description-input');
const $categorySelect = $('#cIds');
const $showAllButton = $('#show-all');

let allItems;

const getTRTemplate = (item) => {
    const categories = item.categories.reduce((prev, cur) => prev + cur.name + "; ", "");
    return `<tr>
                <td>${item.id}</td>
                <td>${item.description}</td>
                <td>${item.created}</td>
                <td>
                    <input type="checkbox" data-id="${item.id}" ${item.done ? 'checked' : ''}>
                </td>
                <td>${item.user.email}</td>
                <td>${categories}</td>
            </tr>`;
}

const renderItems = () => {
    const isShowAll = $showAllButton[0].checked;
    $tbody.empty();
    allItems.forEach(item => {
        if (isShowAll || !item.done) {
            $tbody.append(getTRTemplate(item))
        }
    });
}

const getItems = () => {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/todo/items',
        dataType: 'json'
    }).done((items) => {
        allItems = items;
        renderItems();
    });
};

getItems();

const saveOrUpdate = (item) => {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/todo/items',
        data: item
    }).done((data) => {
        const jsObject = JSON.parse(data);
        const some = allItems.some(currentItem => jsObject.id === currentItem.id);
        if (!some) {
            allItems.push(jsObject);
        }
        renderItems();
    });
};

const validateForm = () => $descriptionInput.val() && $categorySelect.val().length;

$addButton.on(`click`, (evt) => {
    evt.preventDefault();
    if (!validateForm()) {
        alert("All fields must be filled.");
        return;
    }
    saveOrUpdate({
        description: $descriptionInput.val(),
        cIds: $categorySelect.val()
    });
});

$showAllButton.on(`change`, renderItems);

$tbody.on(`change`, ({target}) => {
    const id = target.dataset.id;
    const item = allItems.find(item => item.id === +id);
    item.done = !item.done;
    saveOrUpdate({
        id,
        description: item.description,
        created: item.created,
        done: item.done
    });
    renderItems();
});
