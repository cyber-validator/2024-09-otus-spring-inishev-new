'use client';
import { useState, useEffect } from 'react';
import Head from 'next/head';
import Link from 'next/link';
import { styles } from '@/styles/mainPageStyle';
import { deleteBookById, getAllBooks } from '@/services/bookService';


export default function Home() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch books on component mount
  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await getAllBooks();
        if (!response.ok) {
          throw new Error('Failed to fetch books');
        }
        const data = await response.json();
        setBooks(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  // Handle book deletion
  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this book?')) return;

    try {
      const response = await deleteBookById(id);

      if (!response.ok) {
        throw new Error('Failed to delete book');
      }

      // Remove the book from state
      setBooks(books.filter(book => book.id !== id));
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="container">
      <Head>
        <title>Book Library Management</title>
      </Head>

      <h1>Book Library Management</h1>

      <div className="actions">
        <Link href="/create-book">
          <button className="add-button">Add New Book</button>
        </Link>
      </div>

      <table className="books-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Genres</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.length > 0 ? (
            books.map(book => (
              <tr key={book.id}>
                <td>{book.title}</td>
                <td>{book.author.fullName}</td>
                <td>{book.genres.map(genre => genre.name).join(', ')}</td>
                <td className="actions-cell">
                  <Link href={`/edit-book/${book.id}`}>
                    <button className="edit-button">Edit</button>
                  </Link>
                  <button
                    className="delete-button"
                    onClick={() => handleDelete(book.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" className="no-books">No books found</td>
            </tr>
          )}
        </tbody>
      </table>

      <style jsx>{styles}</style>

    </div>
  );
}