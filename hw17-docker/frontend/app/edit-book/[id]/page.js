'use client';
import { useState, useEffect } from 'react';
import Head from 'next/head';
import Link from 'next/link';
import { styles } from '@/styles/editPageStyle';
import { getAllAuthors, getAllGenres, getBookById, saveBook } from '@/services/bookService';

export default function EditBookPage() {
  const [bookId, setBookId] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    authorId: '',
    genreIds: []
  });
  const [authors, setAuthors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState('');
  const [isLoading, setIsLoading] = useState(true);


  // Get ID from URL when component mounts
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const pathParts = window.location.pathname.split('/');
      const id = pathParts[pathParts.length - 1];
      setBookId(id);
    }
  }, []);

  // Fetch data when bookId is available
  useEffect(() => {
    if (!bookId) return;

    const fetchData = async () => {
      try {
        setIsLoading(true);

        // Fetch authors and genres
        const authorsRes = await getAllAuthors();
        const genresRes = await getAllGenres();

        if (!authorsRes.ok || !genresRes.ok) {
          throw new Error('Failed to fetch data');
        }

        const [authorsData, genresData] = await Promise.all([
          authorsRes.json(),
          genresRes.json()
        ]);

        setAuthors(authorsData);
        setGenres(genresData);

        // Fetch book data
        const bookRes = await getBookById(bookId);
        const bookData = await bookRes.json();

        setFormData({
          title: bookData.title,
          authorId: bookData.author.id,
          genreIds: bookData.genres.map(genre => genre.id)
        });
      } catch (error) {
        setSubmitMessage(error.message || 'Failed to load data');
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [bookId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleGenreChange = (genreId) => {
    setFormData(prev => ({
      ...prev,
      genreIds: prev.genreIds.includes(genreId)
        ? prev.genreIds.filter(id => id !== genreId)
        : [...prev.genreIds, genreId]
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.genreIds.length === 0) {
      setSubmitMessage('Please select at least one genre');
      return;
    }

    setIsSubmitting(true);
    setSubmitMessage('');

    try {
      const response = await saveBook(bookId, formData);

      if (response.ok) {
        setSubmitMessage('Book updated successfully!');
        setFormData({
          title: '',
          authorId: '',
          genreIds: []
        });
        setTimeout(() => window.location.href = '/', 1000);
      } else {
        const errorData = await response.json();
        setSubmitMessage(errorData.message || 'Update failed');
      }
    } catch (error) {
      setSubmitMessage('Network error: ' + error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="container">
        <Head>
          <title>Loading...</title>
        </Head>
        <div>Loading book data...</div>
      </div>
    );
  }

  return (
    <div className="container">
      <Head>
        <title>Edit Book</title>
      </Head>

      <h1>Edit Book</h1>

      {submitMessage && (
        <div className={`message ${submitMessage.includes('success') ? 'success' : 'error'}`}>
          {submitMessage}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="authorId">Author:</label>
          <select
            id="authorId"
            name="authorId"
            value={formData.authorId}
            onChange={handleChange}
            required
          >
            <option value="">Select an author</option>
            {authors.map(author => (
              <option key={author.id} value={author.id}>
                {author.fullName}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Genres:</label>
          <div className="genres-container">
            {genres.map(genre => (
              <div key={genre.id} className="genre-checkbox">
                <input
                  type="checkbox"
                  id={`genre-${genre.id}`}
                  checked={formData.genreIds.includes(genre.id)}
                  onChange={() => handleGenreChange(genre.id)}
                />
                <label htmlFor={`genre-${genre.id}`}>{genre.name}</label>
              </div>
            ))}
          </div>
          {formData.genreIds.length === 0 && (
            <p className="error-text">Please select at least one genre</p>
          )}
        </div>

        <div className="form-actions">
          <button
            type="submit"
            disabled={isSubmitting || formData.genreIds.length === 0}
          >
            {isSubmitting ? 'Updating...' : 'Update Book'}
          </button>
          <Link href="/">
            <button type="button" className="cancel-button">
              Cancel
            </button>
          </Link>
        </div>
      </form>

      <style jsx>{styles}</style>

    </div>
  );
}