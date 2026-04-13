export default function StarRating({ rating, max = 5 }) {
  return (
    <span className="text-yellow-400 text-sm" aria-label={`${rating} su ${max} stelle`}>
      {Array.from({ length: max }, (_, i) => (
        <span key={i}>{i < Math.round(rating) ? '★' : '☆'}</span>
      ))}
    </span>
  )
}
