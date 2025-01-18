package test.project.bookshop.dto.book;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns) {

    @Override
    public String toString() {
        return "BookSearchParametersDto{"
                + "titles=" + (titles == null ? "null" : String.join(", ", titles))
                + ", authors=" + (authors == null ? "null" : String.join(", ", authors))
                + ", isbns=" + (isbns == null ? "null" : String.join(", ", isbns)) + '}';
    }
}
