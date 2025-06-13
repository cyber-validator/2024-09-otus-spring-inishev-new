const baseUrl = '/api/';

export const getAllBooks = async () => await fetch(`${baseUrl}books`);
export const getBookById = async (id) => await fetch(`${baseUrl}books/${id}`);
export const deleteBookById = async (id) => await fetch(`${baseUrl}books/${id}`, { method: 'DELETE',});
export const saveBook = async (bookId, formData) => await fetch(`${baseUrl}books`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: bookId,
          title: formData.title,
          authorId: parseInt(formData.authorId),
          genreIds: formData.genreIds.map(id => parseInt(id))
        })
      });
export const getAllAuthors = async () => await fetch(`${baseUrl}authors`);
export const getAllGenres = async () => await fetch(`${baseUrl}genres`);
