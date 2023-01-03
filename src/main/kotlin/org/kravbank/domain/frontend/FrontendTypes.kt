import com.fasterxml.jackson.annotation.JsonProperty

typealias Banks = List<Bank>

data class Bank(
    val id: String,
    val title: String,
    val description: String,
    val needs: List<Need>,
    val codelist: List<Codelist>,
    val products: List<Product>,
    val publications: List<Publication>,
    val tags: List<Tag>,
    val version: Long,
    val publishedDate: String?,
    val type: String,
    val inheritedBanks: List<Any?>,
    val sourceOriginal: Any?,
    val sourceRel: Any?,
    val projectId: String?,
    val deletedDate: Any?,
)

data class Need(
    val id: String,
    val title: String,
    val description: String,
    val requirements: List<Requirement>,
    val type: String,
    val parent: String,
    val sourceOriginal: String?,
    val sourceRel: Any?,
)

data class Requirement(
    val id: String,
    val title: String,
    val description: String,
    val needId: String,
    val type: String,
    val variants: List<Variant>,
    val tags: List<Any?>?,
    val sourceOriginal: String?,
    val sourceRel: Any?,
    val weight: Long?,
    @JsonProperty("requirement_Type")
    val requirementType: String?,
)

data class Variant(
    val id: String,
    val requirementText: String,
    val instruction: String,
    val useProduct: Boolean,
    val useSpesification: Boolean,
    val useQualification: Boolean,
    val products: List<String>,
    val questions: List<Question>,
    val type: String?,
    val description: String?,
)

data class Question(
    val id: String,
    val type: String,
    val config: Config?,
    val answer: Answer?,
    val sourceRel: Any?,
    val sourceOriginal: Any?,
)

data class Config(
    val min: Long?,
    val max: Long?,
    val step: Double?,
    val unit: String?,
    val defaultPoint: Long?,
    val scoreValues: List<ScoreValue>?,
    val fromBoundary: Any?,
    val toBoundary: Any?,
    val isPeriod: Boolean?,
    val periodMin: Long?,
    val periodMax: Long?,
    val dateScores: List<DateScore>?,
    val periodMinutes: Long?,
    val periodHours: Long?,
    val timeScores: List<TimeScore>?,
    val pointsUnconfirmed: Long?,
    val pointsNonPrefered: Long?,
    val preferedAlternative: Boolean?,
    val discountNonPrefered: Long?,
    val discountPrefered: Long?,
    val defaultDiscount: Long?,
    val mandatoryCodes: List<Any?>?,
    val optionalCodes: List<Any?>?,
    val codelist: String?,
    val codes: List<Any?>?,
    val optionalCodeMinAmount: Long?,
    val optionalCodeMaxAmount: Long?,
    val template: Any?,
    val uploadInSpec: Boolean?,
    val allowMultipleFiles: Boolean?,
    val fileEndings: List<Any?>?,
    val specMin: Long?,
    val specMax: Long?,
    val fromDate: String?,
    val toDate: String?,
    val multipleSelect: Boolean?,
    val fromTime: String?,
    val toTime: String?,
    val discountValues: List<Any?>?,
    val duration: Long?,
    val weekdays: List<Any?>?,
    val discount: Long?,
)

data class ScoreValue(
    val score: Long,
    val value: Long,
)

data class DateScore(
    val date: Any?,
    val score: Long,
)

data class TimeScore(
    val time: Any?,
    val score: Long,
)

data class Answer(
    val point: Long?,
    val value: Any?,
    val fromDate: Any?,
    val toDate: Any?,
    val fromTime: Any?,
    val toTime: Any?,
    val discount: Long?,
    val text: String?,
    val codes: List<Any?>?,
    val files: List<String>?,
)

data class Codelist(
    val id: String,
    val title: String,
    val description: String,
    val codes: List<Code>,
    val type: String,
    val sourceOriginal: String?,
    val sourceRel: Any?,
)

data class Code(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val sourceOriginal: String?,
    val sourceRel: Any?,
    val parent: String?,
)

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val parent: String,
    val sourceOriginal: String?,
    val sourceRel: Any?,
    val deletedDate: String?,
    val unit: String?,
    val requirements: List<Requirement>?,
)

data class Publication(
    val id: String,
    val bankId: String,
    val comment: String,
    val date: String,
    val type: String,
    val version: Long,
    val sourceOriginal: Any?,
    val sourceRel: Any?,
    val deletedDate: String?,
)

data class Tag(
    val id: String,
    val title: String,
    val description: String?,
    val type: String,
    val parent: String,
    val sourceOriginal: String,
    val sourceRel: Any?,
)
