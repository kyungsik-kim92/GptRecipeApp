import java.util.ArrayList

data class GPTModel(
    val choices: ArrayList<ChoiceModel> = ArrayList()
)

data class ChoiceModel(
    val message: MessageModel = MessageModel(),
    val finish_reason: String? = null
)

data class MessageModel(
    val role: String? = null,
    val content: String? = null
)