import css from 'styled-jsx/css'
export const styles = css`
        .container {
          max-width: 800px;
          margin: 0 auto;
          padding: 20px;
        }
        h1 {
          margin-bottom: 20px;
        }
        .form-group {
          margin-bottom: 20px;
        }
        label {
          display: block;
          margin-bottom: 8px;
          font-weight: bold;
        }
        input[type="text"], select {
          width: 100%;
          padding: 10px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 16px;
        }
        .genres-container {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
          gap: 10px;
          margin-top: 10px;
        }
        .genre-checkbox {
          display: flex;
          align-items: center;
          gap: 8px;
        }
        .genre-checkbox input {
          margin: 0;
        }
        .error-text {
          color: #dc3545;
          font-size: 14px;
          margin-top: 5px;
        }
        .form-actions {
          display: flex;
          gap: 10px;
          margin-top: 20px;
        }
        button {
          padding: 10px 20px;
          border-radius: 4px;
          cursor: pointer;
          font-size: 16px;
          border: none;
        }
        button[type="submit"] {
          background-color: #28a745;
          color: white;
        }
        button[type="submit"]:hover {
          background-color: #218838;
        }
        button[type="submit"]:disabled {
          background-color: #6c757d;
          cursor: not-allowed;
        }
        .cancel-button {
          background-color: #dc3545;
          color: white;
        }
        .cancel-button:hover {
          background-color: #c82333;
        }
        .message {
          padding: 10px 15px;
          margin-bottom: 20px;
          border-radius: 4px;
        }
        .success {
          background-color: #d4edda;
          color: #155724;
          border: 1px solid #c3e6cb;
        }
        .error {
          background-color: #f8d7da;
          color: #721c24;
          border: 1px solid #f5c6cb;
        }
      `
