import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.designpatterntest.data.model.Review
import com.example.designpatterntest.databinding.ItemReviewBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.reviewerName.text = review.reviewerName
            binding.reviewerEmail.text = review.reviewerEmail
            binding.ratingBar.rating = review.rating.toFloat()
            binding.reviewComment.text = review.comment
            binding.reviewDate.text = formatDate(review.date)
        }
        private fun formatDate(isoDate: String): String {
            val instant = Instant.parse(isoDate)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.getDefault())
            return instant.atZone(ZoneId.systemDefault()).format(formatter)
        }
    }

}