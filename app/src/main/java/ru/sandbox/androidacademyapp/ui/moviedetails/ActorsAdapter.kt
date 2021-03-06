package ru.sandbox.androidacademyapp.ui.moviedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.sandbox.androidacademyapp.R
import ru.sandbox.androidacademyapp.data.db.entities.Actor

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

    private val image: ImageView = itemView.findViewById(R.id.actor_image)
    private val name: TextView = itemView.findViewById(R.id.actor_full_name)

    fun onBind(actor: Actor) {
        Glide.with(context)
            .load(actor.pictureUrl)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .centerCrop()
            .into(image)
        name.text = actor.name
    }
}

private val RecyclerView.ViewHolder.context
    get() = this.itemView.context
