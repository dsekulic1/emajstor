import React, { useEffect, useState } from 'react'
import { VictoryChart } from 'victory'
import { VictoryBar } from 'victory'
import Box from '@material-ui/core/Box'
import CircularProgress from '@mui/material/CircularProgress'

export default function Chart({ reviews }) {
  const [reviewStats, setReviewStats] = useState([])
  const [loading, setLoading] = useState(true)
  useEffect(() => {
    const getCount = (array, value) => {
      const newArray = array.filter((x) => x.numStars === value)
      return newArray.length
    }
    const data = []
    for (var i = 1; i < 6; i++) {
      const value = getCount(reviews, i)
      data.push({
        numstars: i,
        count: value,
      })
    }
    setReviewStats(data)
    setLoading(false)
  }, [reviews])

  return (
    <Box style={{ height: '95%', marginTop: '5px' }}>
      <h3>Review Statistics</h3>
      {loading ? (
        <CircularProgress />
      ) : (
        <VictoryChart domainPadding={20}>
          <VictoryBar
            data={reviewStats}
            // data accessor for x values
            x='numstars'
            // data accessor for y values
            y='count'
          />
        </VictoryChart>
      )}
    </Box>
  )
}
