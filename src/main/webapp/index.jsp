<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
          crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
            integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <title>TODO list</title>
</head>
<body>
<div class="container mt-3">
    <form>
        <div class="form-group">
            <label>
                <input id="description-input" class="form-control" type="text"
                       name="description" placeholder="description">
            </label>
        </div>
        <button id="add-item-button" class="btn btn-primary">Add</button>
    </form>
    <div class="form-check mt-3">
        <label>
            <input id="show-all" class="form-check-input" type="checkbox">
            Show all
        </label>
    </div>
    <table class="table table-dark">
        <thead>
        <tr>
            <th>id</th>
            <th>description</th>
            <th>created</th>
            <th>done</th>
        </tr>
        </thead>
        <tbody id="item-body">
        </tbody>
    </table>
</div>
<script>
    const $tbody = $('#item-body');
    const $addButton = $('#add-item-button');
    const $descriptionInput = $('#description-input');
    const $showAllButton = $('#show-all');

    let allItems;

    const getTRTemplate = (item) => {
        return `<tr>
                    <td>${item.id}</td>
                    <td>${item.description}</td>
                    <td>${item.created}</td>
                    <td>
                        <input type="checkbox" data-id="${item.id}" ${item.done ? 'checked':''}>
                    </td>
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

    $addButton.on(`click`, (evt) => {
        evt.preventDefault();
        saveOrUpdate({
            description: $descriptionInput.val()
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
</script>
</body>
</html>
