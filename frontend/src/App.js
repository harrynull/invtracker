import './App.css';
import {useEffect, useState} from "react";

const BASE_URL = 'http://localhost:8080/';

function sendRequest(url, body, callback, method) {
    fetch(BASE_URL + url, {
            method: method ?? 'POST',
            credentials: 'include',
            headers: typeof body === 'string' ? {'Content-Type': 'application/json'} : {},
            body
        }
    )
        .then((resp) => resp.json())
        .then(callback)
}

function App() {
    const [inventory, setInventory] = useState([]);
    const [itemTitle, setItemTitle] = useState('');
    const [itemSku, setItemSku] = useState('');
    const [itemDescription, setItemDescription] = useState('');
    const [itemPriceCents, setItemPriceCents] = useState(0);
    const [itemQuantity, setItemQuantity] = useState(0);

    const [showInactive, setShowInactive] = useState(false);
    const [outOfStockOnly, setOutOfStockOnly] = useState(false);

    const [quantityChange, setQuantityChange] = useState(0);

    const refresh = (res) => window.location.reload();
    const tryDelete = (sku) => {
        sendRequest('inventory/' + sku, null, refresh, 'DELETE')
    };
    const genBody = (active) => {
        return JSON.stringify({
            'sku': itemSku,
            'title': itemTitle,
            'description': itemDescription,
            'quantity': itemQuantity,
            'priceCents': itemPriceCents,
            'active': active ?? true,
        });
    };
    const createItem = () => {
        sendRequest('inventory/', genBody(), refresh, 'POST');
    };
    const updateItem = () => {
        sendRequest('inventory/' + itemSku, genBody(), refresh, 'POST');
    };
    const toggleActive = (sku, active) => {
        sendRequest('inventory/' + sku, JSON.stringify({'active': active}), refresh, 'POST');
    };
    const updateQuantity = (sku) => {
        sendRequest('inventory/' + sku + '/quantity_delta', JSON.stringify(quantityChange), refresh, 'POST');
    };
    useEffect(() => {// fetch inventory
        sendRequest("list_inventory",
            JSON.stringify({'showInactive': showInactive, 'outOfStockOnly': outOfStockOnly}),
            (inv) => setInventory(inv),
            'POST'
        )
    }, [showInactive, outOfStockOnly]);

    return (
        <div>
            <header>
                <h1>Inventory<span style={{color: '#6495ED'}}>Tracker</span></h1>
                <p>
                    This is a simple demonstration of the basic functions of the inventory tracker. It is
                    <span style={{fontWeight: 'bold'}}> not</span> written with robustness and user experience in
                    mind. It's quick and dirty. It assumes that a backend server is running at <i><a
                    href="http://localhost:8080">localhost:8080</a></i>
                </p>
            </header>
            <a href={BASE_URL + 'export_csv'}>Export to csv</a>
            <p>Show inactive: <input type={'checkbox'} onChange={(e) => setShowInactive(e.target.checked)}/>
            </p>
            <p>Out of stock only: <input type={'checkbox'} onChange={(e) => setOutOfStockOnly(e.target.checked)}/>
            </p>

            <div className='gallery'>
                {inventory.length === 0 ? <p>No inventory.</p> : inventory.map((inv) =>
                    <div className='image'>
                        <div className='info'>
                            <p>(sku {inv['sku']}) {inv['title']}</p>
                            <p>Description {inv['description']}</p>
                            <p>Quantity {inv['quantity']}
                                <span style={{'marginLeft': '2em'}}>Delta change</span> <input
                                    onChange={(e) => setQuantityChange(e.target.value)}/>
                                <button onClick={() => updateQuantity(inv['sku'])}>quantity change (stock/sale)</button>
                            </p>
                            <p>Price ${inv['priceCents'] / 100}</p>
                            <button onClick={() => tryDelete(inv['sku'])}>delete</button>
                            <button
                                onClick={() => toggleActive(inv['sku'], !inv['active'])}>{inv['active'] ? 'deactivate' : 'activate'}</button>
                        </div>
                    </div>
                )}
            </div>

            <div>
                <h2>Add/Edit Item</h2>
                <p>SKU: <input onChange={(e) => setItemSku(e.target.value)}/></p>
                <p>Title: <input onChange={(e) => setItemTitle(e.target.value)}/></p>
                <p>Description: <input onChange={(e) => setItemDescription(e.target.value)}/></p>
                <p>Price in cents: <input onChange={(e) => setItemPriceCents(+e.target.value)}/></p>
                <p>Quantity: <input onChange={(e) => setItemQuantity(+e.target.value)}/></p>
                <button onClick={createItem}>Create</button>
                <span> / </span>
                <button onClick={updateItem}>Edit</button>
            </div>
        </div>
    );
}

export default App;
