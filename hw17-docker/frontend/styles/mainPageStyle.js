import css from 'styled-jsx/css'
export const styles = css`
        .container {
          max-width: 1200px;
          margin: 0 auto;
          padding: 20px;
        }
        h1 {
          margin-bottom: 20px;
        }
        .actions {
          margin-bottom: 20px;
          text-align: right;
        }
        .add-button {
          background-color: #4CAF50;
          color: white;
          border: none;
          padding: 10px 15px;
          border-radius: 4px;
          cursor: pointer;
        }
        .add-button:hover {
          background-color: #45a049;
        }
        .books-table {
          width: 100%;
          border-collapse: collapse;
        }
        .books-table th, .books-table td {
          border: 1px solid #ddd;
          padding: 12px;
          text-align: left;
        }
        .books-table th {
          background-color: #f2f2f2;
        }
        .books-table tr:nth-child(even) {
          background-color: #f9f9f9;
        }
        .books-table tr:hover {
          background-color: #f1f1f1;
        }
        .actions-cell {
          display: flex;
          gap: 10px;
        }
        .edit-button {
          background-color: #2196F3;
          color: white;
          border: none;
          padding: 6px 12px;
          border-radius: 4px;
          cursor: pointer;
        }
        .edit-button:hover {
          background-color: #0b7dda;
        }
        .delete-button {
          background-color: #f44336;
          color: white;
          border: none;
          padding: 6px 12px;
          border-radius: 4px;
          cursor: pointer;
        }
        .delete-button:hover {
          background-color: #da190b;
        }
        .no-books {
          text-align: center;
          color: #666;
        }
      `
