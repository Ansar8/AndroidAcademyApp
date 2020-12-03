package ru.sandbox.androidacademyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.sandbox.androidacademyapp.data.models.Actor
import ru.sandbox.androidacademyapp.data.models.Movie

class ActorsAdapter(): RecyclerView.Adapter<ActorViewHolder>() {

    private var actors = listOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.view_holder_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBind(actors[position])
    }

    override fun getItemCount(): Int = actors.size

    fun bindMovies(newActors: List<Actor>) {
        actors = newActors
        notifyDataSetChanged()
    }
}

class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView? = itemView.findViewById(R.id.actor_image)
    private val fullName: TextView? = itemView.findViewById(R.id.actor_full_name)

    fun onBind(actor: Actor) {
        image?.setImageResource(actor.image)
        fullName?.text = actor.fullName
    }
}
