'use client';
import { useState, useEffect, useRef } from 'react';
import Head from 'next/head';
import Link from 'next/link';
import { styles } from '@/styles/createPageStyle';
import { getAllAuthors, getAllGenres, saveBook } from '@/services/bookService';

export default function CreateBookPage() {
  const [formData, setFormData] = useState({
    title: '',
    authorId: '',
    genreIds: []
  });
  const [authors, setAuthors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState('');

  const initialized = useRef(false);

  // Fetch authors and genres on component mount
  useEffect(() => {
    if (!initialized.current) {
      initialized.current = true;
      const fetchData = async () => {
        try {
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
        } catch (err) {
          setSubmitMessage(err.message);
        }
      };
      fetchData();
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleGenreChange = (genreId) => {
    setFormData(prev => {
      const newGenreIds = prev.genreIds.includes(genreId)
        ? prev.genreIds.filter(id => id !== genreId)
        : [...prev.genreIds, genreId];

      return {
        ...prev,
        genreIds: newGenreIds
      };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitMessage('');

    try {
      const response = await saveBook(0, formData);

      if (response.ok) {
        setSubmitMessage('Book created successfully!');
        setFormData({
          title: '',
          authorId: '',
          genreIds: []
        });
        setTimeout(() => window.location.href = '/', 1000);
      } else {
        const errorData = await response.json();
        setSubmitMessage(errorData.message || 'Creation failed');
      }
    } catch (error) {
      setSubmitMessage('Network error: ' + error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container">
      <Head>
        <title>Create New Book</title>
      </Head>

      <h1>Add a New Book</h1>

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
            value={Number(formData.authorId)}
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
            {isSubmitting ? 'Creating...' : 'Create Book'}
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